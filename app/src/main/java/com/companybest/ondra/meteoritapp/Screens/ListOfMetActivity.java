package com.companybest.ondra.meteoritapp.Screens;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.companybest.ondra.meteoritapp.Adapter.RealmRecyclerAdater;
import com.companybest.ondra.meteoritapp.Model.MeteoritModel;
import com.companybest.ondra.meteoritapp.R;
import com.companybest.ondra.meteoritapp.RealmBaseActivity;
import com.companybest.ondra.meteoritapp.Tasks.DownloadTask;
import com.companybest.ondra.meteoritapp.Tasks.ServiceForAsync;

import java.util.Calendar;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

//Activity where all the mateorit are displayed

public class ListOfMetActivity extends RealmBaseActivity {

    private Realm realm;
    private RealmResults<MeteoritModel> meteorits;
    private RealmRecyclerAdater adater;
    private RealmRecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_met_activity);

        realm = Realm.getDefaultInstance();

        setTitle("");

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.companybest.ondra.engineerclicker.Activitis", Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("created", 0) == 0) {

            //When the app is created for the first time get information from JSON instantly and write them to realm

            sharedPreferences.edit().putInt("created", 1).apply();

            new DownloadTask(this).execute();

        } else if (sharedPreferences.getInt("created", 0) == 1) {

            //when the app is created next time set alarmManager to get information from JSON if they are on the internet
            //every day at 20Pm if they are not don't do anything

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent i = new Intent(this, ServiceForAsync.class);
            PendingIntent pi = PendingIntent.getService(this, 0, i, 0);

            AlarmManager alarmManager = (AlarmManager) this
                    .getSystemService(Context.ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }

        //Getting all the data from database and if loaded set adapter to realm recyclerView with it

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
            //Sort From biggest
            meteorits = realm.where(MeteoritModel.class).findAll().sort("mass", Sort.DESCENDING);
            adater = new RealmRecyclerAdater(this, meteorits, true, false);
            recyclerView.setAdapter(adater);

            return true;
        } else if (id == R.id.sort_from_smalles) {
            //Sort From smallest
            meteorits = realm.where(MeteoritModel.class).findAll().sort("mass");
            adater = new RealmRecyclerAdater(this, meteorits, true, false);
            recyclerView.setAdapter(adater);

            return true;
        } else if (id == R.id.number_fragment) {
            //navigating to
            FragmentManager fm = getSupportFragmentManager();
            NumOfMetFragment NumOfMetFragment = new NumOfMetFragment();
            NumOfMetFragment.show(fm, "user");

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
