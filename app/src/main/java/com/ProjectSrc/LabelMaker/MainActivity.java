package com.ProjectSrc.LabelMaker;

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
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * <h1>MainActivity</h1>
 * @author  Asaad Belarbi
 * @version 1.0
 * @since   2020-08-14
 */

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "log";
    private static final int BARCODE_REQUEST_CODE = 0;
    private static final int BLUETOOTH_REQUEST_CODE = 7 ;
    private Dialog mDialog;
    private EditText elongeur, elargeur;
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

        setContentView(R.layout.activity_main);
        setTitle("Etiquette");

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

    /**
     * Cette methode fait appel au pop up "modifier, supprimer" lors du clique sur
     * un element du recyclerView
     *
     * @param position recoit la position de l'element cliqué
     *
     */
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

    /**
     * le button ajouter dans la barre de l'application
     * permet d'ajouter un item
     * il cree DalogFragement et lui envoit la liste chainée
     */
    public void addButtonMenu(MenuItem item)
    {
        DialogFragment dialog = DialogAdd.newInstance();
        dialog.show(getSupportFragmentManager(), "tag");
        Bundle b = new Bundle();

        b.putSerializable("data", (Serializable) dataList);
        dialog.setArguments(b);

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

    /**
     * Conversion du complexRecyclerView en Pdf
     * la methode fait une Capture du ComplexRecyclerView en Bitmap puis le convertit en PDF
     */
    public Bitmap getScreenshotFromRecyclerView(RecyclerView view)
    {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null)
        {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++)
            {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }

        }
        return bigBitmap;
    }
    public void toPdf(MenuItem item)
    {
        elongeur = (EditText)findViewById(R.id.longeur);
        elargeur = (EditText)findViewById(R.id.largeur);
        Bitmap cs = getScreenshotFromRecyclerView(rvDataList);
        int length = 0, width = 0;
        try
        {
            String slongeur = elongeur.getText().toString();
            String slargeur = elargeur.getText().toString();
            length = Integer.parseInt(elongeur.getText().toString());
            width = Integer.parseInt(elargeur.getText().toString());
            if (length != 0 && width != 0)
                cs = scaleBitmap(cs, width, length);
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(this, "longeur et largeurs invalides", Toast.LENGTH_LONG).show();
        }

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
        openPdf("etiquette.pdf");

        Toast.makeText(this, "fichier  pdf géneré  dans le dossier : PDF FOLDER", Toast.LENGTH_LONG).show();

    }

    /**
     * Cette methode transforme le reyclerView en Bitmap et l'envoie à l'imprimante
     *
     */
    @SuppressLint("SetTextI18n")
    public void printBitmap(MenuItem item)
    {
        elongeur = (EditText)findViewById(R.id.longeur);
        elargeur = (EditText)findViewById(R.id.largeur);
        screenBitmap = getScreenshotFromRecyclerView(rvDataList);

        String slongeur = elongeur.getText().toString();
        String slargeur =  elargeur.getText().toString();

        int length = Integer.parseInt(elongeur.getText().toString());
        int width = Integer.parseInt(elargeur.getText().toString());
        if (length != 0 && width != 0)
            screenBitmap = scaleBitmap(screenBitmap, width, length);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Entrer l'Uri de l'imprimante");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.uri_input, (ViewGroup) findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        input.setText(string);

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
        builder.setNeutralButton("Rechercher", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Intent intent = new Intent(MainActivity.this, BluetoothSearch.class);
                startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
                input.setText(string);

            }
        });

        builder.show();
    }

    /**
     * Message de redimensionnement d'une image Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight)
    {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    /**
     * Message du resultat de l'impression
     * @param statue est un boolean false si il y'a eu des erreurs dans l'impression
     */
    private void messageEnd(boolean statue)
    {
        Toast.makeText(this, (statue ? "impression en cours" : "erreur de connexion"), Toast.LENGTH_LONG).show();
    }

    public void openPdf(String filename)
    {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/PDF FOLDER/"+ filename);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

        }
    }
}