package com.companybest.ondra.meteoritapp.Tasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.companybest.ondra.meteoritapp.Model.MeteoritModel;

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

//Task for getting all the information from JSON and writing it into realm database

public class DownloadTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;


    @SuppressLint("StaticFieldLeak")
    private Context context;


    //One constructor for the first time creating so progressDialog can be displayed
    //For most of the devices don't need to be because task is fast,
    //but for better visual and no white screen is better to use
    public DownloadTask(Context context) {
        this.context = context;
    }

    //General constructor for service to access
    public DownloadTask() {
    }

    @Override
    protected void onPreExecute() {

        if (context != null) {
            progressDialog = ProgressDialog.show(context, "", "Stahují se všechny meteority prosím počkejte...", true, true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr;

        try {
            //Construct the URL
            //In URL there is token for this app and filter to get all the data form year 2011 to 2015(last update)
            URL url = new URL("https://data.nasa.gov/resource/y77d-th95.json?$$app_token=JHynTGkWZn1dm5gyxPKNVsdBv&$where=year%20between%20%272010-01-10T00:00:00%27%20and%20%272015-01-10T00:00:00%27");

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
                //Since it's JSON, adding a newline isn't necessary
                //But it does make debugging a  easier if you print out the completed
                //buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            forecastJsonStr = buffer.toString();

            return forecastJsonStr;

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onPostExecute(String result) {


        final Realm realm = Realm.getDefaultInstance();
        try {
            //Create JsonArray from result
            final JSONArray jsonArray = new JSONArray(result);

            //Begin Transaction before for loop so we don't open new for each
            realm.beginTransaction();

            for (int i = 0; i < jsonArray.length(); i++) {
                //Get for each i a JsonObject from JsonArray
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                //We need a meteorit to have a year and a mass(name has everyone)
                if (jsonObject.has("year") && jsonObject.has("mass")) {
                    //Year is a string with a date and time too, but every date is 01-01 and time 00
                    //So we get only a year from it to use
                    String fullDate = jsonObject.getString("year");
                    String justYear = fullDate.substring(0, 4);

                    //Log.i("user", "year : " + String.valueOf(Integer.valueOf(justYear)) + "  mass :  " + jsonObject.getString("mass"));

                    //Create a new or update meteorit with our parameters
                    //(id is never used but for realm is best to have @primaryKey
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
            //Because we are using realm in threads it needs to be close by try/finally
            realm.close();
        }

        if (progressDialog != null && context != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }
}

