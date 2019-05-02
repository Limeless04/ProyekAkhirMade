package com.example.awkow2x.myapplication1;

import android.app.Application;

import io.realm.Realm;

public class RealmContext extends Application {
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
}
