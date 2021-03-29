package com.akhil.jauntparents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

import static com.akhil.jauntparents.DetailsActivity.driveraddn;
import static com.akhil.jauntparents.DetailsActivity.driveremailn;
import static com.akhil.jauntparents.DetailsActivity.drivernamen;
import static com.akhil.jauntparents.DetailsActivity.driverphonen;
import static com.akhil.jauntparents.MainActivity.school;
import static com.akhil.jauntparents.SplashActivity.MyPREFERENCES;

/**
 * Created by Bharath on 14/01/17.
 */

public class DriverActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    TextView dname,demail,dphone,dadd;
    DatabaseReference imgstatus;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dname=  (TextView) findViewById(R.id.dname);
        demail=  (TextView) findViewById(R.id.demail);
        dphone=  (TextView) findViewById(R.id.dphone);
        dadd=  (TextView) findViewById(R.id.daddress);
        img = (ImageView) findViewById(R.id.img);
        dname.setText(sharedpreferences.getString(drivernamen,"none"));
        demail.setText(sharedpreferences.getString(driveremailn,"none"));
        dphone.setText(sharedpreferences.getString(driverphonen,"none"));
        dadd.setText(sharedpreferences.getString(driveraddn,"none"));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        imgstatus = database.getReference().child(school).child("driver").child("imgstatus");
        imgstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();
                String st = map.get("st");
                if(st!=null) {
                    if (st.equals("true")) {
                        String url = map.get("url");
                        Picasso.with(DriverActivity.this).load(url).fit().centerCrop().into(img);
                    } else {
//                    mi.setImageResource(R.drawable.upload_image);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


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
