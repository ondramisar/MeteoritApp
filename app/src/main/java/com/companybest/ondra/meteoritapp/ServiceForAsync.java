package com.companybest.ondra.meteoritapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class ServiceForAsync extends Service {

    public ServiceForAsync() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new DownloadTask().execute();

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
        }

@Override
public IBinder onBind(Intent intent) {
        return null;

        }

}


