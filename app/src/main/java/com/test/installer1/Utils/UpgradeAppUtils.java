package com.test.installer1.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by osvaldo on 8/20/17.
 */

public class UpgradeAppUtils extends AsyncTask<String, Integer, String> {
    private Context context;

    public UpgradeAppUtils() {}
    public UpgradeAppUtils(Context ctx) {
        context = ctx;
    }

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
        Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show();
    }

    public void installApk(String path)
    {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive" );
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("Lofting", "About to install new .apk");
        context.startActivity(i);
    }
}
