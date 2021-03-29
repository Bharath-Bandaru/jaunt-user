package com.akhil.jauntparents;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.akhil.jauntparents.DetailsActivity.phnon;
import static com.akhil.jauntparents.SplashActivity.MyPREFERENCES;

public class MainActivity extends RuntimePermissionsActivity {
    public  Boolean flag =true;
    private TextView txtName;
    private TextView txtEmail;
    public static final String permstatus = "permstatus";
    private static final int REQUEST_PERMISSIONS = 20;
    public static String school="school1";
    private Button btnLogout;
    CardView profile,driver,feedback,schoolcv,track,notifiction;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(school).child("notification");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getBoolean(permstatus, true)) {
            MainActivity.super.requestAppPermissions(new
                            String[]{android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.CHANGE_NETWORK_STATE, android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.INTERNET, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CALL_PHONE}, R.string
                            .runtime_permissions_txt
                    , REQUEST_PERMISSIONS);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(permstatus, false);
            editor.commit();
        }
        if (flag){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d("e", "Value is: " + value);
                    if (value != null) {
                        Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
                        Context context = getApplicationContext();
                        Intent resultIntent = new Intent(context, NotificationActivity.class);
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.ic_account_circle_white_48dp)
                                        .setContentTitle(value)
                                        .setContentText("Hello World!");
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        if (alarmSound == null) {
                            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            if (alarmSound == null) {
                                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            }
                        }

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        stackBuilder.addParentStack(MainActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
                        mBuilder.setContentIntent(contentIntent);
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mBuilder.setSound(alarmSound);
                        mNotificationManager.notify(4, mBuilder.build());

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("e", "Failed to read value.", error.toException());
                }
            });
            flag=false;
    }
        profile=(CardView)findViewById(R.id.cv11);
        driver=(CardView)findViewById(R.id.cv13);
        schoolcv=(CardView)findViewById(R.id.cv23);
        track=(CardView)findViewById(R.id.cv12);
        feedback=(CardView)findViewById(R.id.cv22);
        notifiction=(CardView) findViewById(R.id.cv21);
        notifiction.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(),NotificationActivity.class);
                startActivity(i1);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(i1);
            }
        });
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(getApplicationContext(),DriverActivity.class);
                startActivity(i1);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(),FeedbackActivity.class);
                startActivity(i1);
            }
        });
        schoolcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(),SchoolActivity.class);
                startActivity(i1);
            }
        });
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i1);
            }
        });
//        startService(new Intent(FirebaseBackgroundService.class.getName()));
//        Intent serviceIntent = new Intent(getApplicationContext(),FirebaseBackgroundService.class);
//        getApplicationContext().startService(serviceIntent);
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
    public void onPermissionsGranted(final int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

}