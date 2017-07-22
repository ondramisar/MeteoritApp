package com.companybest.ondra.meteoritapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class RealmRecyclerAdater extends RealmBasedRecyclerViewAdapter<MeteoritModel, RealmRecyclerAdater.ViewHolder> {

    class ViewHolder extends RealmViewHolder {

        TextView name;
        TextView mass;
        TextView year;
        Button next;

        ViewHolder(FrameLayout container) {
            super(container);
            this.name = (TextView) container.findViewById(R.id.name);
            this.mass = (TextView) container.findViewById(R.id.mass);
            this.year = (TextView) container.findViewById(R.id.year);
            this.next = (Button) container.findViewById(R.id.next);


        }
    }

    private static final int[] COLORS = new int[] {
            Color.argb(255, 28, 160, 170),
            Color.argb(255, 99, 161, 247),
            Color.argb(255, 13, 79, 139),
            Color.argb(255, 89, 113, 173),
            Color.argb(255, 99, 214, 74),
            Color.argb(255, 205, 92, 92),
            Color.argb(255, 105, 5, 98)
    };

    public RealmRecyclerAdater(android.content.Context context, RealmResults<MeteoritModel> realmResults, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.item_recycler_view, viewGroup, false);
        ViewHolder vh = new ViewHolder((FrameLayout) v);
        return vh;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindRealmViewHolder(final ViewHolder viewHolder, int position) {
        final MeteoritModel meteoritModel = realmResults.get(position);

        viewHolder.name.setText("Meteorit : " + meteoritModel.getName());
        viewHolder.mass.setText("Velikost : " + String.valueOf(meteoritModel.getMass()) + " g");
        viewHolder.year.setText("Rok : " + String.valueOf(meteoritModel.getYear()));


        //Log.i("heyno", meteoritModel.getLat() + " " + String.valueOf(meteoritModel.getLng()));

        viewHolder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MapActivity.class);
                i.putExtra("lat", meteoritModel.getLat());
                i.putExtra("lng", meteoritModel.getLng());
                getContext().startActivity(i);


            }
        });


        viewHolder.itemView.setBackgroundColor(
                COLORS[(int) (meteoritModel.getId() % COLORS.length)]
        );


    }
}