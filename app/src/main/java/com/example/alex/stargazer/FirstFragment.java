package com.example.alex.stargazer;
import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.data.geojson.GeoJsonLayer;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.EasyPermissions;


public class FirstFragment extends Fragment implements OnMapReadyCallback {
    JSONObject mJson;
    GeoJsonLayer mLayer;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    List<Spot> spots;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Location mLocation;
   // boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.first_fragment, container, false);
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location!=null && mGoogleMap!=null){
                    mLocation = location;

                }

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}



        };
        if ((ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0l,0f, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0l,0f, locationListener);
        } else {
            EasyPermissions.requestPermissions(this, "please give", 0,Manifest.permission.ACCESS_FINE_LOCATION);
            EasyPermissions.requestPermissions(this, "please give", 0,Manifest.permission.ACCESS_COARSE_LOCATION);

        }


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNewLocation(mLocation);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);





        mLocation = new Location("");

        try {
            spots = new DatabaseAsync().execute(null, -1, null, 0.0, 0.0, null).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < spots.size(); i++) {
            mLocation.setLongitude(spots.get(i).getLongitude());
            mLocation.setLatitude(spots.get(i).getLatitude());
            LatLng ll = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
            MarkerOptions options = new MarkerOptions()
                    .position(ll)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(spots.get(i).getName());

            mGoogleMap.addMarker(options);


        }

        try {
            mLayer = new GeoJsonLayer(mGoogleMap, R.raw.map2, getActivity());
            mLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void handleNewLocation(Location location) {
    //    Log.d("TAG", location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MySingletonClass.getInstance().setValue1(latLng);
        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f));

    }


    private class DatabaseAsync extends AsyncTask<Object, Void, List<Spot>> {

        // Constructor providing a reference to the views in MainActivity
        public DatabaseAsync(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Spot> doInBackground(Object... params) {
            AppDatabase db = Room.databaseBuilder(getActivity(), AppDatabase.class, "production").allowMainThreadQueries().build();

            Boolean shouldUpdate = (Boolean) params[0];
            int position = (int) params[1];
            String name = (String) params[2];
            double longitude = (double) params[3];
            double latitude = (double) params[4];
            String info = (String) params[5];

            //check whether to add add or update event based on if shouldUpdate is null
            if (shouldUpdate != null) {
                //update event if shouldUpdate is true
                if (shouldUpdate) {
                    Spot spot = spots.get(position);
                    spot.setName(name);
                    spot.setLongitude(longitude);
                    spot.setLatitude(latitude);
                    spot.setInfo(info);



                    //update event into the database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().updateSpot(spot);
                    //db.textBookDao().updateTextBook(textBook);

                } else {
                    //add event if shouldUpdate is false
                    Spot spot = new Spot(name,longitude,latitude,info);


                    //add event into the database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().addSpot(spot);

                    //db.textBookDao().addTextBook(textBook);

                }

            } else { //so no update since shouldUpdate == null
                //delete all if postion is = -2, really bad, i should fix this
                if (position == -2)
                    //delete all events  from database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().dropTheTable();

                    //db.textBookDao().dropTheTable();
                    //delete event
                else if (position != -1) { //-1 means delete a specific event
                    Spot spot = spots.get(position);

                    //delete event from database
                    AppDatabase.getAppDatabase(getActivity()).spotDao().deleteSpot(spot);

                    //db.textBookDao().deleteTextBook(textBook);
                }
            }

            //get events from database, also not a great way to do this
            spots = AppDatabase.getAppDatabase(getActivity()).spotDao().getAllSpots();

            return spots;

        }
        @Override
        protected void onPostExecute(List<Spot> items) {



            //shows NO EVENTS FOUND when list is empty
        }
    }


}
