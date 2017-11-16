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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloold.buildp.Modules.DirectionFinderListener;
import com.example.bloold.buildp.Modules.DirectionFinder;
import com.example.bloold.buildp.Modules.Route;
import com.example.bloold.buildp.R;
import com.example.bloold.buildp.model.MyItem;
import com.example.bloold.buildp.search.SearchActivity;
import com.example.bloold.buildp.single.object.SingleObjectActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

//import com.google.maps.android.utils.demo.model.MyItem;

public class BigClusteringDemoActivity extends BaseDemoActivity implements  DirectionFinderListener, ClusterManager.OnClusterInfoWindowClickListener, ClusterManager.OnClusterItemInfoWindowClickListener, ClusterManager.OnClusterClickListener, GoogleMap.OnMarkerClickListener, AdapterView.OnItemClickListener {
    private ClusterManager<MyItem> mClusterManager;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    double lng1 = 16.85176189428896;
    double lng2 = 148.6877064739115;
    double lat1 = -30.4486834932737;
    double lat2 = 85.0511287798066;
    double latitude = 45.85176189428896;
    double longitude = 39.6877064739115;
    //private BottomNavigationView mBottomNav;
    private LinearLayout routeInfo;
    private LinearLayout mInfo;
    private LinearLayout llTime;
    private TextView markerAddress;
    private TextView markerName;
    private TextView  tvDist;
    Boolean flag = false;
    Marker oldMarker;
    Intent intentRoute;
    Intent intentSingleObject;
    Intent searchIntent;

    HashMap<String, MyItem> mEventMapItem;
    private ImageView ivWalk;
    private ImageView ivCar;
    private ImageView ivBus;
    private AppCompatImageView ivSearch;
    private ImageView menu_edit;
    private ImageView menu_alert;
    private ImageView menu_camera;
    private ImageView menu_route;
    private Toolbar toolbar;
    private String searchText =null;
    private String searchType =null;
    private Boolean fl = false;

    @Override
    protected void startDemo() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_map);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final Criteria criteria = new Criteria();
        final LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mEventMapItem = new HashMap<String, MyItem>();

        intentRoute = new Intent(BigClusteringDemoActivity.this, RouteActivity.class);
        searchIntent = new Intent(BigClusteringDemoActivity.this, SearchActivity.class);
        intentSingleObject = new Intent(BigClusteringDemoActivity.this, SingleObjectActivity.class);

        mClusterManager = new ClusterManager<MyItem>(this, getMap());

        //getMap().setOnCameraIdleListener(mClusterManager);
        mInfo = (LinearLayout) findViewById(R.id.map_info);
        routeInfo = (LinearLayout) findViewById(R.id.routeInfo);
        markerAddress = (TextView) findViewById(R.id.marker_address);
        markerName = (TextView) findViewById(R.id.marker_name);
        tvDist = (TextView) findViewById(R.id.tvDist);
        ivWalk = (ImageView) findViewById(R.id.ivWalk);
        ivCar = (ImageView) findViewById(R.id.ivCar);
        ivBus = (ImageView) findViewById(R.id.ivBus);
        ivSearch = (AppCompatImageView) findViewById(R.id.ivSearch);

        menu_edit = (ImageView) findViewById(R.id.menu_edit);
        menu_alert = (ImageView) findViewById(R.id.menu_alert);
        menu_camera = (ImageView) findViewById(R.id.menu_camera);
        menu_route = (ImageView) findViewById(R.id.menu_route);

        menu_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intentRoute);
            }
        });
        Intent intent = getIntent();
        final String atvOrigin = intent.getStringExtra("Origin");
        final String atvDestination = intent.getStringExtra("Destination");
        if (atvOrigin != null && atvDestination != null) {
            sendRequest(atvOrigin, atvDestination,"driving");
        }
        searchText = intent.getStringExtra("searchText");
        searchType = intent.getStringExtra("searchType");

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIntent.putExtra("fromActivity", "map");
                startActivity(searchIntent);
            }
        });

        markerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intentSingleObject);
            }
        });

        ivWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest(atvOrigin, atvDestination, "walking");
            }
        });

        ivCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest(atvOrigin, atvDestination,"driving");
            }
        });

        ivBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest(atvOrigin, atvDestination,"transit");
            }
        });
        mClusterManager.setOnClusterItemClickListener(mClusterItemClickListener);
        //getMap().setOnMarkerClickListener(mClusterManager);
        // getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String address = marker.getTitle();
                String name = marker.getSnippet();
                if (address == null && name == null) {
                    return true;
                }

                if (oldMarker != null) {
                    oldMarker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker", 58, 83)));
                }

                markerAddress.setText(address);

                MyItem item = mEventMapItem.get(marker.getId());
                markerName.setText(name);
                mInfo.setVisibility(View.VISIBLE);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("red_marker",58,83)));
                oldMarker = marker;
                String ObjId = item.getObjId();
                String mrLatitude = String.valueOf(marker.getPosition().latitude);
                String mrLongitude = String.valueOf(marker.getPosition().longitude);
                double geoLatitude = 0;
                double geoLongitude = 0;

                if (ActivityCompat.checkSelfPermission(BigClusteringDemoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BigClusteringDemoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                double dist = 0;
                final Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                if(location!=null) {
                    geoLatitude = location.getLatitude();
                    geoLongitude = location.getLongitude();
                    dist = CalculationDistance(geoLatitude,geoLongitude,marker.getPosition().latitude,marker.getPosition().longitude);
                }
                dist = Integer.valueOf((int) dist);
                tvDist.setText(String.valueOf(dist)+"м");
                String mrPosition = String.valueOf(mrLatitude+","+mrLongitude);
                intentRoute.putExtra("mrPosition", mrPosition);
                intentSingleObject.putExtra("ObjId", ObjId);
                String geoPosition = String.valueOf(geoLatitude+","+geoLongitude);
                intentRoute.putExtra("geoPosition", geoPosition);
                intentRoute.putExtra("mrTitle",marker.getSnippet());
                return true;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getMap().setMyLocationEnabled(true);
        final Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        if(location!=null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
        }
        final CameraPosition[] mPreviousCameraPosition = {null};
        getMap().setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                oldMarker = null;
                //getMap().addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                   //     .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("walk",100,100))));
                if(flag==false) {
                    mInfo.setVisibility(View.INVISIBLE);
                    mClusterManager.clearItems();
                    CameraPosition position = getMap().getCameraPosition();
                    ChangeMap();
                    if (mPreviousCameraPosition[0] == null || mPreviousCameraPosition[0].zoom != position.zoom) {
                        mPreviousCameraPosition[0] = getMap().getCameraPosition();
                    }
                }else {
                    flag = false;
                    return;
                }
            }
        });


        mClusterManager.setRenderer(new OwnIconRendered(this.getApplicationContext(), getMap(), mClusterManager));
       // mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
       // mBottomNav.setOnNavigationItemSelectedListener(this);
    }

    public static double CalculationDistance(double startPointLat,double startPointLon,double endPointLat,double endPointLon) {
        return CalculationDistanceByCoord(startPointLat, startPointLon, endPointLat, endPointLon);
    }

    private static double CalculationDistanceByCoord(double startPointLat,double startPointLon,double endPointLat,double endPointLon){
        float[] results = new float[1];
        Location.distanceBetween(startPointLat, startPointLon, endPointLat, endPointLon, results);
        return results[0];
    }
    public ClusterManager.OnClusterItemClickListener<MyItem> mClusterItemClickListener = new ClusterManager.OnClusterItemClickListener<MyItem>() {

        @Override
        public boolean onClusterItemClick(MyItem item) {
            String address = item.getTitle();
            String name = item.getSnippet();
            markerAddress.setText(address);
            markerName.setText(name);
            mInfo.setVisibility(View.VISIBLE);
            return true;
        }
    };


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_items, menu);
        mBottomNav  = (NavigationView) findViewById(R.id.navigation);
        mBottomNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
        return true;
    }*/

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public boolean onClusterClick(Cluster cluster) {
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster cluster) {

    }

    @Override
    public void onClusterItemInfoWindowClick(ClusterItem clusterItem) {

    }

  /*  @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit: {
            }
            break;
            case R.id.menu_alert: {
            }
            break;
            case R.id.menu_camera: {
            }
            break;
            case R.id.menu_route: {
                startActivity(intentRoute);
            }
            break;
        }
        return true;
    }*/


    class OwnIconRendered extends DefaultClusterRenderer<MyItem> {

        public OwnIconRendered(Context context, GoogleMap map,
                               ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onClusterItemRendered(MyItem item, Marker marker) {
            super.onClusterItemRendered(item, marker);
            mEventMapItem.put(marker.getId(), item);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker",58,83)));
            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);

        }
        @Override
        protected int getColor(int clusterSize) {
            return Color.parseColor("#65bacc");
        }
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    private void ChangeMap(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LatLngBounds bounds = getMap().getProjection().getVisibleRegion().latLngBounds;
            lat1 = bounds.southwest.latitude;
            lat2 = bounds.northeast.latitude;

            lng1 = bounds.southwest.longitude;
            lng2 = bounds.northeast.longitude;
            if (lat1<lat2){
                double tmp  = lat1;
                lat1 = lat2;
                lat1 =tmp;
            }

            if (lng1<lng2){
                double tmp  = lng1;
                lng1 = lng2;
                lng1 =tmp;
            }
        } else {
           return;
        }
        String JSONTaskUrl ="";
        String search = "";
        String filter = "";
        if (searchText !=null && searchType !=null){
            search=searchType;
            try {
                search = URLEncoder.encode(searchText, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(searchType.equals("object")){
                filter = "&filter[%3FNAME]=";
            }
            if(searchType.equals("egrn")){
                filter = "&filter[%3FEGRKN_NUMBER]=";
            }
            if(searchType.equals("address")){
                filter = "&filter[%3FADDRESS]=";
            }
            JSONTaskUrl = "http://ruinnet.idefa.ru/api_app/object/index/?select[]=OBJECT_ID&select[]=NAME&select[]=ADDRESS&select[]=MAP_TYPE&select[]=LAT&select[]=LNG&filter[INCLUDE_SUBSECTIONS]=Y"+filter+search+"&filter[%3C%3DLAT_FROM]="+lat2+"&filter[%3E%3DLAT_TO]="+lat1+"&filter[%3C%3DLNG_FROM]="+lng2+"&filter[%3E%3DLNG_TO]="+lng1+"&limit=3500&page=1";
        }else{
            JSONTaskUrl = "http://ruinnet.idefa.ru/api_app/object/index/?select[]=OBJECT_ID&select[]=NAME&select[]=ADDRESS&select[]=MAP_TYPE&select[]=LAT&select[]=LNG&filter[INCLUDE_SUBSECTIONS]=Y&filter[%3C%3DLAT_FROM]="+lat2+"&filter[%3E%3DLAT_TO]="+lat1+"&filter[%3C%3DLNG_FROM]="+lng2+"&filter[%3E%3DLNG_TO]="+lng1+"&limit=3500&page=1";

        }
        new JSONTask().execute(JSONTaskUrl);
    }


    private void sendRequest(String origin , String destination, String mode) {
        try {
            new DirectionFinder(this, origin, destination, mode).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDirectionFinderStart() {
        if(fl==false) {
            progressDialog = ProgressDialog.show(BigClusteringDemoActivity.this, "", "", true);
            fl = true;
        }

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        if(fl==true) {
            progressDialog.dismiss();
            fl = false;
        }
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();


        for (Route route : routes) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
           // ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            ((TextView) findViewById(R.id.tvTime)).setText(route.duration.text+", "+route.distance.text);
            llTime = (LinearLayout) findViewById(R.id.llTime);
            llTime.setVisibility(View.VISIBLE);

            originMarkers.add(getMap().addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("red_marker",58,83)))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add( getMap().addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("red_marker",58,83)))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add( getMap().addPolyline(polylineOptions));
            routeInfo.setVisibility(View.VISIBLE);

        }
    }
    public class JSONTask extends AsyncTask<String,String, List<MyItem> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(fl==false) {
                progressDialog = ProgressDialog.show(BigClusteringDemoActivity.this, "", "", true);
                fl = true;
            }
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
                return items;
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
        protected void onPostExecute( List<MyItem> result) {
            mClusterManager.cluster();
            progressDialog.dismiss();
            if(fl==true) {
                progressDialog.dismiss();
                fl = false;
            }
            super.onPostExecute(result);
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


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BigClusteringDemoActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(BigClusteringDemoActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BigClusteringDemoActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(BigClusteringDemoActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
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