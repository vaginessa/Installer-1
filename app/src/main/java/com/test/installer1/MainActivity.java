package com.test.installer1;

import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.test.installer1.Utils.UpgradeAppUtils;

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

    public void installAPK(View view) {
        UpgradeAppUtils task = new UpgradeAppUtils(getApplicationContext());
        task.execute("http://192.168.0.3:8080/app-debug.apk");
    }
}
