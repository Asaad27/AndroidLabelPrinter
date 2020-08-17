package com.ProjectSrc.LabelMaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.intermec.print.lp.LinePrinter;
import com.intermec.print.lp.LinePrinterException;
import com.intermec.print.lp.PrintProgressEvent;
import com.intermec.print.lp.PrintProgressListener;

import java.io.File;

import static android.content.ContentValues.TAG;

public class PrintActivity extends Activity
{
    private LinePrinter  lp;
    private Bitmap bitmap;
    private String uri;
    private boolean connectStatue = true;
    private Context context;

    public void createPrintTask(String uri, Bitmap bitmap, Context context)
    {
        this.bitmap = bitmap;
        this.uri = uri;
        this.context = context;
        PrintTask task = new PrintTask();

        // Executes PrintTask with the specified parameters which will
        // be passed to the PrintTask.doInBackground method. In this
        // case, the doInBackgroud method does not expect any parameter.
        task.execute();
    }

    public boolean isConnectStatue()
    {
        return connectStatue;
    }

    public void setConnectStatue(boolean connectStatue)
    {
        this.connectStatue = connectStatue;
    }

    class PrintTask extends AsyncTask<Void, Integer, Void>
    {
        /**
         * This method runs on a background thread. The specified parameters
         * are the parameters passed to the execute method by the caller of
         * this task. This method can call publishProgress to publish updates
         * on the UI thread.
         */

        @Override
        protected Void doInBackground(Void... args)
        {
            doPrint();
            return null;
        }

        /**
         * Runs on the UI thread after publishProgress is invoked. The
         * specified values are the values passed to publishProgress.
         */
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            // Access the values array.
            int progress = values[0];

            switch (progress)
            {
                case PrintProgressEvent.MessageTypes.CANCEL:
                    Log.d(TAG, "Printing cancelled");
                    break;
                case PrintProgressEvent.MessageTypes.COMPLETE:
                    Log.d(TAG, "Printing completed");
                    break;
                case PrintProgressEvent.MessageTypes.ENDDOC:
                    Log.d(TAG, "ENDDOC");
                    break;
                case PrintProgressEvent.MessageTypes.FINISHED:
                    Log.d(TAG, "Printer connection closed");
                    break;
                case PrintProgressEvent.MessageTypes.STARTDOC:
                    break;
                default:
                    Log.e(TAG, "onProgressUpdate: " );
                    break;
            }
        }

        private void doPrint()
        {
            LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
            exSettings.setContext(PrintActivity.this);


            try
            {
                // Creates a LinePrinter object with the specified
                // parameters. The URI string "bt://00:02:5B:00:02:78"
                // specifies to connect to the printer via Bluetooth
                // and the Bluetooth MAC address.


                File profiles = new File (context.getExternalFilesDir(null), "printer_profiles.JSON");
                lp = new LinePrinter(profiles.getAbsolutePath(), "PR2",
                        "bt://" + uri, exSettings);

                lp.addPrintProgressListener(new PrintProgressListener() {
                    public void receivedStatus(PrintProgressEvent aEvent)
                    {
                        // Publishes updates on the UI thread.
                        publishProgress(aEvent.getMessageType());
                    }
                });
                lp.connect();  // Connects to the printer
                String base64String = ImageUtil.convert(bitmap);
                lp.writeGraphicBase64(base64String, 0, 0, bitmap.getWidth(), bitmap.getHeight());

            }
            catch (LinePrinterException ex)
            {
                Log.e(TAG, "doPrint: " + ex.getMessage());
                connectStatue = false;
            }
            catch (Exception ex)
            {
                Log.e(TAG, "doPrint: " + ex.getMessage());
                connectStatue = false;
            }
            finally
            {
                if (lp != null)
                {
                    try
                    {
                        lp.disconnect();  // Disconnects from the printer
                        lp.close();  // Releases resources
                    }
                    catch (Exception ex) {connectStatue = false;}
                }
            }
        }
    }


}

