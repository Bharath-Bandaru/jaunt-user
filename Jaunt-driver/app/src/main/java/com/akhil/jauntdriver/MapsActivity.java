package com.akhil.jauntdriver;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
        FirebaseDatabase database;
        DatabaseReference myRef;
        private static final String TAG = "LocationActivity";
        private static final long INTERVAL = 1000 * 1 * 1; //1 minute
        private static final long FASTEST_INTERVAL = 1000 * 1 * 1; // 1 minute
        Button btnFusedLocation;
        TextView tvLocation;
        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;
        Location mCurrentLocation;
        String mLastUpdateTime;
        GoogleMap googleMap;

        protected void createLocationRequest() {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(INTERVAL);
                mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Log.d(TAG, "onCreate ...............................");
                //show error dialog if GoolglePlayServices not available
                if (!isGooglePlayServicesAvailable()) {
                        finish();
                }
                createLocationRequest();
                // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(AppIndex.API).build();

                setContentView(R.layout.activity_location_google_map);
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                if (fm != null) {
                        fm.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap map) {
                                        loadMap(map);
                                }
                        });
                } else {
                        Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
                }
        }

        protected void loadMap(GoogleMap googleMa) {
                googleMap = googleMa;
                if (googleMap != null) {
                        // Map is ready
                        googleMap.getUiSettings().setZoomControlsEnabled(true);

                        Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
//                        MapDemoActivityPermissionsDispatcher.getMyLocationWithCheck(this);
                } else {
                        Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
                }
        }

        @Override
        public void onStart() {
                super.onStart();
                Log.d(TAG, "onStart fired ..............");
                mGoogleApiClient.connect();
                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
                AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
        }

        @Override
        public void onStop() {
                super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
                AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
                Log.d(TAG, "onStop fired ..............");
                mGoogleApiClient.disconnect();
                Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        }

        private boolean isGooglePlayServicesAvailable() {
                int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (ConnectionResult.SUCCESS == status) {
                        return true;
                } else {
                        GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
                        return false;
                }
        }

        @Override
        public void onConnected(Bundle bundle) {
                Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
                startLocationUpdates();
        }

        protected void startLocationUpdates() {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        return;
                }
                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
                Log.d(TAG, "Location update started ..............: ");
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
                Log.d(TAG, "Connection failed: " + connectionResult.toString());
        }

        @Override
        public void onLocationChanged(Location location) {
                Log.d(TAG, "Firing onLocationChanged..............................................");
                mCurrentLocation = location;
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                addMarker();
        }

        private void addMarker() {
                MarkerOptions options = new MarkerOptions();

                // following four lines requires 'Google Maps Android API Utility Library'
                // https://developers.google.com/maps/documentation/android/utility/
                // I have used this to display the time as title for location markers
                // you can safely comment the following four lines but for this info
//                IconGenerator iconFactory = new IconGenerator(this);
//                iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
//                options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
//                options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

                LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("loc");
                myRef.setValue(mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude());
                options.position(currentLatLng);
                Marker mapMarker = googleMap.addMarker(options);
                long atTime = mCurrentLocation.getTime();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
                mapMarker.setTitle(mLastUpdateTime);
                Log.d(TAG, "Marker added.............................");
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                        13));
                Log.d(TAG, "Zoom done.............................");

        }

        @Override
        protected void onPause() {
                super.onPause();
                stopLocationUpdates();
        }

        protected void stopLocationUpdates() {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
                Log.d(TAG, "Location update stopped .......................");
        }

        @Override
        public void onResume() {
                super.onResume();
                if (mGoogleApiClient.isConnected()) {
                        startLocationUpdates();
                        Log.d(TAG, "Location update resumed .....................");
                }
        }

        /**
         * ATTENTION: This was auto-generated to implement the App Indexing API.
         * See https://g.co/AppIndexing/AndroidStudio for more information.
         */
        public Action getIndexApiAction() {
                Thing object = new Thing.Builder()
                        .setName("Maps Page") // TODO: Define a title for the content shown.
                        // TODO: Make sure this auto-generated URL is correct.
                        .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                        .build();
                return new Action.Builder(Action.TYPE_VIEW)
                        .setObject(object)
                        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                        .build();
        }
}