package com.companybest.ondra.meteoritapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class NumberOfMeteoritsFragment extends DialogFragment {

    Realm realm;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.number_meteorit_fragment, container,
                false);

        realm = Realm.getDefaultInstance();

        TextView numberOfAll = (TextView) rootView.findViewById(R.id.number_of_met);
        TextView numberOf2011 = (TextView) rootView.findViewById(R.id.number_2011);
        TextView numberOf2012 = (TextView) rootView.findViewById(R.id.number_2012);
        TextView numberOf2013 = (TextView) rootView.findViewById(R.id.number_2013);

        RealmResults<MeteoritModel> allMet = realm.where(MeteoritModel.class).findAll();
        numberOfAll.setText("Od roku 2011 až do roku 2013 spadlo: " + String.valueOf(allMet.size()) + " meteoritů");

        allMet = realm.where(MeteoritModel.class).equalTo("year", 2011).findAll();
        numberOf2011.setText("V roce 2011 spadlo: " + String.valueOf(allMet.size()) + " meteoritů");


        allMet = realm.where(MeteoritModel.class).equalTo("year", 2012).findAll();
        numberOf2012.setText("V roce 2012 spadlo: " + String.valueOf(allMet.size()) + " meteoritů");


        allMet = realm.where(MeteoritModel.class).equalTo("year", 2013).findAll();
        numberOf2013.setText("V roce 2013 spadlo: " + String.valueOf(allMet.size()) + " meteoritů");

        Button creditBack = (Button) rootView.findViewById(R.id.creditBack);
        creditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberOfMeteoritsFragment.this.dismiss();
            }
        });
        // Do something else
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}