package com.akhil.jauntparents;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.akhil.jauntparents.MainActivity.school;

/**
 * Created by Bharath on 14/01/17.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker mark = null;
    private CameraPosition cameraPosition;
    private MarkerOptions marker;
    private boolean keeplooping = true;
    private boolean firstTime = true ;
    private boolean isMarkerRotating = false;
    SupportMapFragment mapFragment;
    DatabaseReference myRef;
    String value,longi,lati;
    String a[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(school).child("loc");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initilizeMap();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void initilizeMap() {
        if (googleMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            // check if map is created successfully or not

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }
        try {


            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(),"Please provide location permissions in App Settings.",Toast.LENGTH_LONG).show();
                return;
            }
            googleMap.setMyLocationEnabled(false);

            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

        } catch (Exception e) {

            e.printStackTrace();
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = dataSnapshot.getValue(String.class);
//                Map<String,String> map=(Map) dataSnapshot.getValue();
//                 lati =map.get("lati");
//                 longi =map.get("longi");

                if(keeplooping) {
                    getLatLong();
                }
                Log.d("0", "Value is: " + longi+lati);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("0", "Failed to read value.", error.toException());
            }
        });




//        final Timer t = new Timer();
//
//        t.scheduleAtFixedRate(
//                new TimerTask()
//                {
//                    public void run()
//                    {
//                        if(keeplooping) {
//                            getLatLong();
//                        }
//                        else {
//                            t.purge();
//                            t.cancel();
//                        }
//                    }
//                },
//                0,      // run first occurrence immediatetly
//                10000);
//
//


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getLatLong(){
        if(value!=null) {
            a=value.split(",");
            changeMarkerPosition(Double.parseDouble(a[0]), Double.parseDouble(a[1]));
        }
        else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void changeMarkerPosition(double latitude, double longitude) {


        if (!firstTime) {

            //  Toast.makeText(getApplicationContext(),"second time"+googleMap.getCameraPosition().zoom,Toast.LENGTH_SHORT).show();

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude,
                            longitude)).zoom(googleMap.getCameraPosition().zoom).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            LatLng newPosition = new LatLng(latitude, longitude);

            Projection proj = googleMap.getProjection();
            Point startPoint = proj.toScreenLocation(mark.getPosition());
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);

            float bearing = (float) bearingBetweenLocations(startLatLng, newPosition);
            rotateMarker(mark, bearing);
            //Toast.makeText(getApplicationContext(),newPosition.toString(),Toast.LENGTH_SHORT).show();
            animateMarker(newPosition, false);

        } else {
            // Adding a marker
            //Toast.makeText(getApplicationContext(),"first time",Toast.LENGTH_SHORT).show();
            firstTime = false;

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude,
                            longitude)).zoom(16).build();

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude))
                    .title("I'm here!")
                    .snippet("Arriving!");

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));

            mark = googleMap.addMarker(marker);

        }
    }

    public void animateMarker(final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(mark.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                mark.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        mark.setVisible(false);
                    } else {
                        mark.setVisible(true);
                    }
                }
            }
        });
    }
    private void rotateMarker(final Marker marker, final float toRotation) {

        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    float bearing =  -rot > 180 ? rot / 2 : rot;

                    marker.setRotation(bearing);

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }
    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    @Override
    public void onBackPressed(){

        //   Intent i = new Intent(MyOrdersActivity.this, HomeScreenActivity.class);
        //  startActivity(i);
        keeplooping = false;
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
