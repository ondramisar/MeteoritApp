package com.companybest.ondra.meteoritapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;


public class DownloadTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;


    @SuppressLint("StaticFieldLeak")
    private Context context = null;


    public DownloadTask(Context context) {
        Log.i("user4", "context not null in const");
        this.context = context;
    }

    public DownloadTask() {

    }

    @Override
    protected void onPreExecute() {

        if (context != null) {
            Log.i("user4", "context not null");
            progressDialog = ProgressDialog.show(context, "", "Stahují se všechny meteority prosím počkejte...", true, true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        //Log.i("user2", String.valueOf(progressDialog.getProgress()));

    }

    @Override
    protected String doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try {
            // Construct the URL
            // URL url = new URL("https://data.nasa.gov/resource/y77d-th95.json?$$app_token=JHynTGkWZn1dm5gyxPKNVsdBv");
            URL url = new URL("https://data.nasa.gov/resource/y77d-th95.json?$where=year%20between%20%272010-01-10T00:00:00%27%20and%20%272015-01-10T00:00:00%27");
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {

                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            return forecastJsonStr;
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {


        final Realm realm = Realm.getDefaultInstance();
        try {
            final JSONArray jsonArray = new JSONArray(s);

            realm.beginTransaction();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject;
                jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("year") && jsonObject.has("mass")) {
                    String fullDate = jsonObject.getString("year");

                    final String justYear = fullDate.substring(0, 4);

                    Log.i("user", "year : " + String.valueOf(Integer.valueOf(justYear)) + "  mass :  " + jsonObject.getString("mass"));

                    //Log.i("heyno","reclat : " + String.valueOf(Double.valueOf(jsonObject.getString("reclat")))+ "  reclong :  " + String.valueOf(Double.valueOf(jsonObject.getString("reclong"))));

                    MeteoritModel meteorit = new MeteoritModel();
                    meteorit.setId(Integer.parseInt(jsonObject.getString("id")));
                    meteorit.setYear(Integer.parseInt(justYear));
                    meteorit.setName(jsonObject.getString("name"));
                    meteorit.setMass(Float.valueOf(jsonObject.getString("mass")));
                    meteorit.setLat(Double.valueOf(jsonObject.getString("reclat")));
                    meteorit.setLng(Double.valueOf(jsonObject.getString("reclong")));
                    realm.copyToRealmOrUpdate(meteorit);
                }
            }

            realm.commitTransaction();

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }

        if (progressDialog != null && context != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }
}

