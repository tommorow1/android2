/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bloold.buildp.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.bloold.buildp.R;
import com.example.bloold.buildp.model.MyItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;
//import com.google.maps.android.utils.demo.model.MyItem;

public class BigClusteringDemoActivity extends BaseDemoActivity {
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.227104, 39.728221), 5));

        mClusterManager = new ClusterManager<MyItem>(this, getMap());

        getMap().setOnCameraIdleListener(mClusterManager);
        //readItems();
        new JSONTask().execute("http://ruinnet.idefa.ru/api_app/object/index/?select[]=ID&select[]=NAME&select[]=ADDRESS&select[]=MAP_TYPE&select[]=LAT&select[]=LNG&filter[INCLUDE_SUBSECTIONS]=Y&filter[%3C%3DLAT_FROM]=85.0511287798066&filter[%3E%3DLAT_TO]=-30.4486834932737&filter[%3C%3DLNG_FROM]=148.6877064739115&filter[%3E%3DLNG_TO]=16.85176189428896&limit=2147483647&page=1");
    }

    public class JSONTask extends AsyncTask<String,String, List<MyItem> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected List<MyItem> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Device-Id","0000");
                String header = "Basic " + new String(android.util.Base64.encode("defa:defa".getBytes(), android.util.Base64.NO_WRAP));
                connection.addRequestProperty("Authorization", header);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONArray ITEMS = null;
                JSONObject DATA = null;
                JSONObject response = null;
                response = new JSONObject(finalJson);
                try {
                    DATA = new JSONObject(response.getString("DATA"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    ITEMS = new JSONArray(DATA.getString("ITEMS"));
                    //parentArray = DATA.getJSONArray("ITEMS");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<MyItem> items = new MyItemReader().read(ITEMS);
                mClusterManager.addItems(items);
                /*for (int i = 0; i < 10; i++) {
                    double offset = i / 60d;
                    for (MyItem item : items) {
                        LatLng position = item.getPosition();
                        double lat = position.latitude + offset;
                        double lng = position.longitude + offset;
                        MyItem offsetItem = new MyItem(lat, lng);
                        mClusterManager.addItem(offsetItem);
                    }
                }*/

                } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

    }

   /* private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            for (MyItem item : items) {
                LatLng position = item.getPosition();
                double lat = position.latitude + offset;
                double lng = position.longitude + offset;
                MyItem offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);
            }
        }
    }*/
}