package com.companybest.ondra.meteoritapp;


import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//So we can initialized realm and set up configuration

public abstract class RealmBaseActivity extends AppCompatActivity {

    private RealmConfiguration realmConfiguration;

    protected RealmConfiguration getRealmConfig() {
        Realm.init(this);

        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration
                    .Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
        }

        return realmConfiguration;
    }

    protected void resetRealm() {
        Realm.deleteRealm(getRealmConfig());
    }
}