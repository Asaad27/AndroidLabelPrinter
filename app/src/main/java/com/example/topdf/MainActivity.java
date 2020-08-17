package com.example.topdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "log";
    private static final int BARCODE_REQUEST_CODE = 0;
    private static final int BLUETOOTH_REQUEST_CODE = 7 ;
    private Dialog mDialog;
    private static DataList dataList;
    private String uri;
    private Bitmap screenBitmap;
    private String string = "xx:xx:xx:xx:xx:xx";


    public static ComplexRecyclerViewAdapter complexRecyclerViewAdapter;
    private RecyclerView rvDataList;
    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }*/
        setContentView(R.layout.activity_main);
        setTitle("Etiquette");
        //getActionBar().setIcon(R.drawable.my_icon);

        dataList = new DataList();
        rvDataList = findViewById(R.id.rvContacts);
        complexRecyclerViewAdapter = new ComplexRecyclerViewAdapter(dataList);
        rvDataList.setAdapter(complexRecyclerViewAdapter);
        rvDataList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        rvDataList.addItemDecoration(itemDecor);
        this.configureOnClickRecyclerView();
        mDialog = new Dialog(this);
    }

    private void configureOnClickRecyclerView()
    {
        ItemClickSupport.addTo(rvDataList, R.layout.activity_main)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener()
                {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v)
                    {
                        //Log.e("TAG", "Position : "+position);
                        showPop(position);
                    }
                });
    }

    public void showPop(final int position)
    {
        Button bEdit, bDelete;
        mDialog.setContentView(R.layout.pop);
        dataList.dialogEditRowPosition = position;
        bEdit = (Button)mDialog.findViewById(R.id.bModifier);
        bDelete = (Button)mDialog.findViewById(R.id.bSupprimer);
        bDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dataList.deleteNode(position);
                complexRecyclerViewAdapter.notifyDataSetChanged();
                mDialog.dismiss();
            }
        });
        bEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragment dialog = DialogEdit.newInstance();
                dialog.show(getSupportFragmentManager(), "tag");
                Bundle b = new Bundle();

                b.putSerializable("data", (Serializable) dataList);
                dialog.setArguments(b);
            }
        });
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addButtonMenu(MenuItem item)
    {
        DialogFragment dialog = DialogAdd.newInstance();
        dialog.show(getSupportFragmentManager(), "tag");
        Bundle b = new Bundle();

        b.putSerializable("data", (Serializable) dataList);
        dialog.setArguments(b);

        //Log.d(TAG, "addButtonMenu: " + dataList.getType(0));
    }

    public void bareCodeActivity(MenuItem item)
    {
        Intent intent = new Intent(this, BareCode.class);
        //intent.putExtra("data", dataList);
        startActivityForResult(intent, BARCODE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                Label label = (Label) data.getSerializableExtra("dataRes");
                dataList.addBarcode(label);
                complexRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == BLUETOOTH_REQUEST_CODE)
        {
            if (data != null)
                this.string = (String) data.getStringExtra("code");
            printBitmap(null);

        }

    }

    public void toPdf(MenuItem item)
    {
        LinearLayout ll;

        ll = (LinearLayout)findViewById(R.id.linear);
        ll.setDrawingCacheEnabled(true);
        ll.buildDrawingCache(true);
        Bitmap cs = Bitmap.createBitmap(ll.getDrawingCache());
        ll.setDrawingCacheEnabled(false);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(cs.getWidth(), cs.getHeight(), 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pi);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(cs, cs.getWidth(), cs.getHeight(), true);
        canvas.drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        File root = new File(Environment.getExternalStorageDirectory(), "PDF FOLDER");
        if (!root.exists())
        {
            root.mkdirs();
        }
        File pdfFile = new File(root, "etiquette.pdf");
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(fileOutputStream);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(this, "fichier  pdf géneré  dans le dossier : PDF FOLDER", Toast.LENGTH_LONG).show();

    }


    @SuppressLint("SetTextI18n")
    public void printBitmap(MenuItem item)
    {
        LinearLayout ll;

        ll = (LinearLayout)findViewById(R.id.linear);
        ll.setDrawingCacheEnabled(true);
        ll.buildDrawingCache(true);
        screenBitmap= Bitmap.createBitmap(ll.getDrawingCache());
        ll.setDrawingCacheEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Entrer l'Uri de l'imprimante");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.uri_input, (ViewGroup) findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        input.setText(string);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                uri = input.getText().toString();
                PrintActivity printActivity = new PrintActivity();
                printActivity.createPrintTask(uri, screenBitmap, MainActivity.this);
                messageEnd(printActivity.isConnectStatue());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("afficher la liste", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Intent intent = new Intent(MainActivity.this, BluetoothSearch.class);
                //intent.putExtra("data", dataList);
                startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
                input.setText(string);

            }
        });

        builder.show();
    }


    private void messageEnd(boolean statue)
    {
        Toast.makeText(this, (statue ? "impression en cours" : "erreur de connexion"), Toast.LENGTH_LONG).show();
    }
}