package com.ProjectSrc.LabelMaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BareCode extends AppCompatActivity
{

    private static final String TAG = "tag";
    private ImageView qrCode;
    private EditText qrInput;
    private Button bValidate, bGenerate;
    private CheckBox checkBoxQr, checkBoxCodabar, checkBoxUpc, checkBoxCode128;
    private Bitmap imageBitmap;
    private Label label;
    private ProxyBitmap proxyTest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcecode);
        checkBoxUpc     = findViewById(R.id.checkBoxUpc);
        checkBoxCode128 = findViewById(R.id.checkBoxCode128);
        checkBoxQr      = findViewById(R.id.checkBoxQr);
        checkBoxCodabar = findViewById(R.id.checkBoxCodabar);
        qrCode          = findViewById(R.id.bar_code);
        qrInput         = findViewById(R.id.data_text);
        bValidate       = findViewById(R.id.validerCodeBar);
        bGenerate       = findViewById(R.id.button_generate);

        Intent intent = getIntent();
        //dataList = (DataList) intent.getSerializableExtra("data");

    }

    private void generateQRCode_general(String data) throws WriterException
    {
        BarcodeFormat barcodeFormat = getFormat();
        com.google.zxing.Writer writer = new MultiFormatWriter();
        String finalData = Uri.encode(data, "utf-8");
        int height = 150, width = 150;

        if (barcodeFormat != BarcodeFormat.QR_CODE)
            height = 30;
        try
        {
            BitMatrix bm = writer.encode(finalData, barcodeFormat, width, height);
            imageBitmap = Bitmap.createBitmap(150, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                    imageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
            }
            proxyTest = new ProxyBitmap(imageBitmap);
            Bitmap bitmapTest = proxyTest.getBitmap();
            qrCode.setImageBitmap(bitmapTest);
        }
        catch (IllegalStateException e)
        {
            Toast.makeText(this, "Format du Code Entrée incorrecte", Toast.LENGTH_LONG).show();
        }

    }

    public void buttonGenerate(View view) throws WriterException
    {
        String data = qrInput.getText().toString();
        Log.d(TAG, "buttonGenerate: " + data);
        try
        {
            generateQRCode_general(data);
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(this, "Format du Code Entrée incorrecte", Toast.LENGTH_LONG).show();
        }

    }

    public void buttonValidate(View view)
    {
        //System.out.println(dataList.getSimpleTitle(0, 0));
        Toast.makeText(this, "Codebar ajouté", Toast.LENGTH_LONG).show();
        //Log.d(TAG, "buttonValidate: " + dataList.getType(0));
        //dataList.addBarcode(proxyTest);
        label = new Label(0, new String[]{""}, new String[]{""}, proxyTest);
        Intent newIntent = new Intent();
        newIntent.putExtra("dataRes", label);
        setResult(RESULT_OK, newIntent);
        finish();

    }

    public BarcodeFormat getFormat()
    {
        if (checkBoxCodabar.isChecked())
            return BarcodeFormat.CODABAR;
        else if (checkBoxQr.isChecked())
            return BarcodeFormat.QR_CODE;
        else if (checkBoxCode128.isChecked())
            return BarcodeFormat.CODE_128;
        else if (checkBoxUpc.isChecked())
            return BarcodeFormat.UPC_A;
        else
            return BarcodeFormat.QR_CODE;
    }

   public void onCheckBoxClick(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        int id = view.getId();
        if (checked)
        {
            if (checkBoxCodabar.isChecked() && R.id.checkBoxCodabar != id)
                checkBoxCodabar.setChecked(false);
            else if (checkBoxQr.isChecked() && R.id.checkBoxQr != id)
                checkBoxQr.setChecked(false);
            else if (checkBoxCode128.isChecked() && R.id.checkBoxCode128 != id)
                checkBoxCode128.setChecked(false);
            else if (checkBoxUpc.isChecked() && R.id.checkBoxUpc != id)
                checkBoxUpc.setChecked(false);
        }

    }


}