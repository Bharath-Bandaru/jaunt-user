package com.akhil.jauntparents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.akhil.jauntparents.DetailsActivity.princn;
import static com.akhil.jauntparents.DetailsActivity.scaddn;
import static com.akhil.jauntparents.DetailsActivity.scemailn;
import static com.akhil.jauntparents.DetailsActivity.scphonen;
import static com.akhil.jauntparents.MainActivity.school;
import static com.akhil.jauntparents.SplashActivity.MyPREFERENCES;

/**
 * Created by Bharath on 14/01/17.
 */

public class SchoolActivity extends AppCompatActivity {
    TextView scpri,scph,scemail,scadd;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference prin = database.getReference().child(school).child("prinicipal");
        DatabaseReference ad = database.getReference().child(school).child("address");
        DatabaseReference ema = database.getReference().child(school).child("email");
        DatabaseReference phn = database.getReference().child(school).child("phone");

        scpri = (TextView) findViewById(R.id.princname);
        scemail = (TextView) findViewById(R.id.pemail);
        scph = (TextView)findViewById(R.id.pphnone);
        scadd = (TextView) findViewById(R.id.schooladd);
        scpri.setText(sharedpreferences.getString(princn,"none"));
        scph.setText(sharedpreferences.getString(scphonen,"none"));
        scemail.setText(sharedpreferences.getString(scemailn,"none"));
        scadd.setText(sharedpreferences.getString(scaddn,"none"));

        // Read from the database

    }
    public void onClickCallSchool(View view){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+sharedpreferences.getString(scphonen,"none")));
        startActivity(callIntent);
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
