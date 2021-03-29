package com.akhil.jauntparents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.akhil.jauntparents.DetailsActivity.emailn;
import static com.akhil.jauntparents.DetailsActivity.parentnamen;
import static com.akhil.jauntparents.DetailsActivity.phnon;
import static com.akhil.jauntparents.MainActivity.school;
import static com.akhil.jauntparents.SplashActivity.MyPREFERENCES;

/**
 * Created by Bharath on 14/01/17.
 */

public class FeedbackActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    EditText fname,femail,fphno,feed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        fname = (EditText) findViewById(R.id.fname);
        femail = (EditText) findViewById(R.id.fmail);
        fphno = (EditText) findViewById(R.id.fphone);
        feed = (EditText) findViewById(R.id.feed);

        fname.setText(sharedpreferences.getString(parentnamen,""));
        femail.setText(sharedpreferences.getString(emailn,""));
        fphno.setText(sharedpreferences.getString(phnon,""));
    }

    public Boolean check(){

        if(feed.getText().toString().equals("")){
            Toast.makeText(this, "Enter Feedback", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public void onClickFeed(View v){
       if(check()){
           FirebaseDatabase database = FirebaseDatabase.getInstance();
           DatabaseReference myRef = database.getReference().child(school).child("feedback");
           myRef.child(sharedpreferences.getString(phnon,"none")).child("fname").setValue(fname.getText().toString());
           myRef.child(sharedpreferences.getString(phnon,"none")).child("fphno").setValue(fphno.getText().toString());
           myRef.child(sharedpreferences.getString(phnon,"none")).child("femail").setValue(femail.getText().toString());
           myRef.child(sharedpreferences.getString(phnon,"none")).child("feed").setValue(feed.getText().toString());
           Toast.makeText(this, "Submitted succesfully", Toast.LENGTH_SHORT).show();
       }
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
