package com.companybest.ondra.meteoritapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListOfMetActivity extends RealmBaseActivity {

    Realm realm;
    RealmResults<MeteoritModel> meteorits;
    RealmRecyclerAdater adater;
    RealmRecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_met_activity);


        realm = Realm.getDefaultInstance();

        setTitle("");

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.companybest.ondra.engineerclicker.Activitis", Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("created", 0) == 0) {
            Log.i("user4", "first time");
            sharedPreferences.edit().putInt("created", 1).apply();
            new DownloadTask(this).execute();
        } else if (sharedPreferences.getInt("created", 0) == 1) {
            Log.i("user4", "second time");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent i = new Intent(this, ServiceForAsync.class);
            PendingIntent pi = PendingIntent.getService(this, 0, i, 0);

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        meteorits = realm.where(MeteoritModel.class).findAll().sort("mass", Sort.DESCENDING);


        recyclerView = (RealmRecyclerView) findViewById(R.id.realm_recycler_view);

        if (meteorits.isLoaded()) {
            adater = new RealmRecyclerAdater(this, meteorits, true, false);
            recyclerView.setAdapter(adater);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sort_from_biggest) {
            meteorits = realm.where(MeteoritModel.class).findAll().sort("mass", Sort.DESCENDING);
            adater = new RealmRecyclerAdater(this, meteorits, true,false);
            recyclerView.setAdapter(adater);
        } else if (id == R.id.sort_from_smalles) {
            meteorits = realm.where(MeteoritModel.class).findAll().sort("mass");
            adater = new RealmRecyclerAdater(this, meteorits, true,false);
            recyclerView.setAdapter(adater);
        } else if (id == R.id.number_fragment){
            FragmentManager fm = getSupportFragmentManager();
            NumberOfMeteoritsFragment numberOfMeteoritsFragment = new NumberOfMeteoritsFragment();
            numberOfMeteoritsFragment.show(fm, "user");
        return  true;

        }
        return super.onOptionsItemSelected(item);
    }

}
