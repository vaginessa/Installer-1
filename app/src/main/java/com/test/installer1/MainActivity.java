package com.test.installer1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        Log.d("Test", "" + Environment.getExternalStorageDirectory());
    }

    private void installApk(String path)
    {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive" );
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("Lofting", "About to install new .apk");
        getApplicationContext().startActivity(i);
    }

    public void installAPK(View view) {
        DownloadAPK task = new DownloadAPK();
        task.execute("http://192.168.0.3:8080/app-debug.apk");
    }

    public class DownloadAPK extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String[] params) {
            String path = Environment.getExternalStorageDirectory() + "/Download/app-debug.apk"; //"/sdcard/Download/app-debug.apk";
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("YourApp", "Well that didn't work out so well...");
                Log.e("YourApp", "" + e);
                return "";
            }
            return path;
        }

        @Override
        protected void onPostExecute(String path)
        {
            if (path != null)
            {
                if (!path.equals(""))
                {
                    installApk(path);
                }
            }
            Toast.makeText(getApplicationContext(), "Download finished", Toast.LENGTH_SHORT).show();
        }
    }
}
