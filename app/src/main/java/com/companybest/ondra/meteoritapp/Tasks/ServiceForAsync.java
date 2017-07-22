package com.companybest.ondra.meteoritapp.Tasks;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

//Service is only for async to get called by alarmManager

public class ServiceForAsync extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Check for internet, If not than don't update
        if (isNetworkAvailable()) {
            new DownloadTask().execute();
        }

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


