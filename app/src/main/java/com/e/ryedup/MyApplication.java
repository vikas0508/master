package com.e.ryedup;

import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //firebase
        FirebaseApp.initializeApp(this);
    }
}
