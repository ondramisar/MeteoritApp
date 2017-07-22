package com.companybest.ondra.meteoritapp.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.companybest.ondra.meteoritapp.R;
import com.companybest.ondra.meteoritapp.RealmBaseActivity;

import io.realm.Realm;

public class MainActivity extends RealmBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("");

        //Set configuration that will all the realm instances have
        Realm.setDefaultConfiguration(getRealmConfig());

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfMetActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
