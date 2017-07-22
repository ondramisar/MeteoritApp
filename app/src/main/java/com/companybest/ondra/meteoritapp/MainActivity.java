package com.companybest.ondra.meteoritapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends RealmBaseActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.setDefaultConfiguration(getRealmConfig());

        realm = Realm.getDefaultInstance();

        setTitle("");

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.companybest.ondra.engineerclicker.Activitis", Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("created", 0) == 0) {
            Log.i("user4", "first time");
            sharedPreferences.edit().putInt("created", 1).apply();
            new DownloadTask(getApplicationContext()).execute();
        } else if (sharedPreferences.getInt("created", 0) == 1) {
            Log.i("user4", "second time");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 19);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 59);

            Intent i = new Intent(this, ServiceForAsync.class);
            PendingIntent pi = PendingIntent.getService(this,0, i,0);

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        RealmResults<MeteoritModel> meteorits = realm.where(MeteoritModel.class).findAll().sort("mass", Sort.DESCENDING);


        RealmRecyclerView recyclerView = (RealmRecyclerView) findViewById(R.id.realm_recycler_view);

        if (meteorits.isLoaded()) {
            RealmRecyclerAdater adater = new RealmRecyclerAdater(this, meteorits, true, false);
            recyclerView.setAdapter(adater);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
