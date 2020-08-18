package com.ProjectSrc.LabelMaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * BluetoothSearche permet la recherche bluetooth et l'affichage de la liste de periphériques
 * lien de l'api : https://developer.android.com/guide/topics/connectivity/bluetooth
 */
public class BluetoothSearch extends AppCompatActivity
{
    private ListView btListView;
    private TextView textViewStatus;
    private Button search;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> bluetoothDevices = new ArrayList<>();
    private ArrayList<String> bluetoothAdresses = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    static final int IMAGING = BluetoothClass.Device.Major.IMAGING;
    static final int COMPUTER = BluetoothClass.Device.Major.COMPUTER;
    static final int PHONE = BluetoothClass.Device.Major.PHONE;
    public static int REQUEST_BLUETOOTH = 1;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            //Log.i("ACTION", action);

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                textViewStatus.setText("recherche terminée");
                search.setEnabled(true);
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("found");
                String name = device.getName();
                String adress = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                //Log.i("device found", "Name : " + name + "Adress : " + adress + "RSSI : " + rssi);
                String deviceString = "";
                if (device.getBluetoothClass().getMajorDeviceClass() == IMAGING)
                    deviceString += "Type : Imprimante - ";
                else if (device.getBluetoothClass().getMajorDeviceClass() == COMPUTER)
                    deviceString += "Type : Ordinateur - ";
                else if (device.getBluetoothClass().getMajorDeviceClass() == PHONE)
                    deviceString += "Type : Smartphone - ";
                else
                    deviceString += "Type : Autre - ";

                if (name == null || name.equals(""))
                    deviceString = adress;
                else
                    deviceString = name + " : " + adress;

                deviceString += " - RSSI : " + rssi;
                if (!bluetoothDevices.contains(deviceString))
                {
                    bluetoothAdresses.add(adress);
                    bluetoothDevices.add(deviceString);
                }
                arrayAdapter.notifyDataSetChanged();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                textViewStatus.setText("recherche en cours");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_search);

        btListView = findViewById(R.id.btListView);
        search = findViewById(R.id.bt_search);
        textViewStatus = findViewById(R.id.textViewStatus);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(this, "bluetooth not supported", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            if (!bluetoothAdapter.isEnabled())
            {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);
            btListView.setAdapter(arrayAdapter);

            arrayAdapter.notifyDataSetChanged();
            btListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    String string = bluetoothAdresses.get(i);
                    Intent newIntent = new Intent();
                    newIntent.putExtra("code", string);
                    setResult(RESULT_OK, newIntent);
                    finish();
                }
            });

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(broadcastReceiver, intentFilter);

        }
    }

    @SuppressLint("SetTextI18n")
    public void searchButton(View view)
    {
        search.setEnabled(false);
        bluetoothDevices.clear();
        bluetoothAdresses.clear();

        bluetoothAdapter.startDiscovery();

    }
}