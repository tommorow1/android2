package com.example.bloold.buildp.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloold.buildp.R;
import com.example.bloold.buildp.common.IntentHelper;
import com.example.bloold.buildp.ui.RouteMapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RouteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView tvRoute;
    private AutoCompleteTextView atvOrigin;
    private AutoCompleteTextView atvDestination;
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBsqnZcch_56fMZAFOeO_eSh2nsqqLFWGY";
    String mrPosition = null;
    String geoPosition = null;
    private static String mrTitle = null;
    private Toolbar toolbar;
    String PosCoods = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            tvTitle.setText("Построить маршрут");
            tvTitle.setVisibility(View.VISIBLE);
        }
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
             getSupportActionBar().setTitle("Маршрут");

        }*/
        Intent intentRoute = getIntent();
        mrPosition = intentRoute.getStringExtra("mrPosition");
        geoPosition = intentRoute.getStringExtra("geoPosition");
        mrTitle = intentRoute.getStringExtra("mrTitle");

        atvOrigin = (AutoCompleteTextView) findViewById(R.id.atvOrigin);
        atvDestination = (AutoCompleteTextView) findViewById(R.id.atvDestination);
        if(mrTitle!=null && mrPosition!=null ){
            atvDestination.setText(mrTitle);
        }
        if(geoPosition!=null && geoPosition!="00"){
            atvOrigin.setText("Мое местоположение");
        }

        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origin = geoPosition;
                String destination = mrPosition;
                if (origin==null) {
                    Toast.makeText(RouteActivity.this, "Адрес пуст", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (destination==null) {
                    Toast.makeText(RouteActivity.this, "Адрес пуст", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(RouteActivity.this, RouteMapActivity.class)
                    .putExtra(IntentHelper.Companion.getEXTRA_ORIGIN(), origin)
                        .putExtra(IntentHelper.Companion.getEXTRA_DESTINATION(), destination));
            }
        });
        AutoCompleteTextView autoCompView = findViewById(R.id.atvOrigin);
        autoCompView.setAdapter(new RouteActivity.GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener((parent, v, position, id) -> {
            String selection = (String)parent.getItemAtPosition(position);
            try {
                 new JSONGet().execute(selection).get();
                Thread.sleep(1000);
                 geoPosition = PosCoods;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });

        final AutoCompleteTextView autoCompView2 = (AutoCompleteTextView) findViewById(R.id.atvDestination);
        autoCompView2.setAdapter(new RouteActivity.GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
                String selection = (String)parent.getItemAtPosition(position);

                try {
                    new JSONGet().execute(selection).get();
                    Thread.sleep(1000);
                    mrPosition = PosCoods;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public class JSONGet extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result ="";
            try {
                String input = params[0];
                URL url = new URL("http://ruinnet.idefa.ru/api_app/object/index/?select[]=LAT&select[]=LNG&select[]=NAME&filter[0][?NAME]=" + URLEncoder.encode(input, "utf8"));

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Device-Id","0000");
                String header = "Basic " + new String(android.util.Base64.encode("defa:defa".getBytes(), android.util.Base64.NO_WRAP));
                connection.addRequestProperty("Authorization", header);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line ="";
                StringBuilder responseOutput = new StringBuilder();
                while((line = reader.readLine()) != null ) {
                    responseOutput.append(line);
                }
                result = responseOutput.toString();
                String finalJson = result.toString();
                JSONObject response = null;
                try {
                    response = new JSONObject(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response !=null) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(result.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject DATA = null;
                    try {
                        DATA = new JSONObject(jsonObj.getString("DATA"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray predsJsonArray = null;
                    try {
                        predsJsonArray = DATA.getJSONArray("ITEMS");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        PosCoods = predsJsonArray.getJSONObject(0).getString("LAT") + "," + predsJsonArray.getJSONObject(0).getString("LNG");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;

        }

    }

   /* @SuppressLint("LongLogTag")
    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }*/
   @SuppressLint("LongLogTag")
   public static ArrayList autocomplete(String input) {
       ArrayList resultList = null;

       HttpURLConnection conn = null;
       StringBuilder jsonResults = new StringBuilder();
       try {
           StringBuilder sb = new StringBuilder("http://ruinnet.idefa.ru/api_app/object/index/?select[]=NAME&filter[0][?NAME]=" + URLEncoder.encode(input, "utf8"));

           URL url = new URL(sb.toString());
           conn = (HttpURLConnection) url.openConnection();
           InputStreamReader in = new InputStreamReader(conn.getInputStream());

           // Load the results into a StringBuilder
           int read;
           char[] buff = new char[1024];
           while ((read = in.read(buff)) != -1) {
               jsonResults.append(buff, 0, read);
           }
       } catch (MalformedURLException e) {
           Log.e(LOG_TAG, "Error processing Places API URL", e);
           return resultList;
       } catch (IOException e) {
           Log.e(LOG_TAG, "Error connecting to Places API", e);
           return resultList;
       } finally {
           if (conn != null) {
               conn.disconnect();
           }
       }

       try {
           // Create a JSON object hierarchy from the results
           JSONObject jsonObj = new JSONObject(jsonResults.toString());

           JSONObject DATA = new JSONObject(jsonObj.getString("DATA"));
           JSONArray predsJsonArray = DATA.getJSONArray("ITEMS");

           // Extract the Place descriptions from the results
           resultList = new ArrayList(predsJsonArray.length());
           for (int i = 0; i < predsJsonArray.length(); i++) {
               System.out.println(predsJsonArray.getJSONObject(i).getString("NAME"));
               System.out.println("============================================================");
               resultList.add(predsJsonArray.getJSONObject(i).getString("NAME"));
           }
       } catch (JSONException e) {
           Log.e(LOG_TAG, "Cannot process JSON results", e);
       }

       return resultList;
   }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
