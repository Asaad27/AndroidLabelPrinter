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

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Log.i("ACTION", action);

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                textViewStatus.setText("recherche d'imprimantes terminée");
                search.setEnabled(true);
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String name = device.getName();
                String adress = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                //Log.i("device found", "Name : " + name + "Adress : " + adress + "RSSI : " + rssi);
                String deviceString = "";
                if (device.getBluetoothClass().getMajorDeviceClass() == IMAGING)
                    deviceString += "Imprimante : ";

                if (name != null || name.equals(""))
                    deviceString = adress;
                else
                    deviceString = name + " : " + adress;
                if (!bluetoothDevices.contains(deviceString))
                {
                    bluetoothAdresses.add(adress);
                    bluetoothDevices.add(deviceString);
                }
                arrayAdapter.notifyDataSetChanged();
            }
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

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);
        btListView.setAdapter(arrayAdapter);

        /*bluetoothDevices.add("test");
        bluetoothAdresses.add("test2");*/
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

    @SuppressLint("SetTextI18n")
    public void searchButton(View view)
    {
        textViewStatus.setText("Entrain de rechercher...");
        search.setEnabled(false);
        bluetoothDevices.clear();
        bluetoothAdresses.clear();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(this, "bluetooth not supported", Toast.LENGTH_LONG).show();
            finish();
        }
        else
            bluetoothAdapter.startDiscovery();
    }
}