package com.akhil.jauntparents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.akhil.jauntparents.MainActivity.school;
import static com.akhil.jauntparents.SplashActivity.MyPREFERENCES;
import static com.akhil.jauntparents.SplashActivity.openstatus;

/**
 * Created by Bharath on 14/01/17.
 */

public class DetailsActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    EditText cname,pname,phno,addr,email,cls;
    public static final String childnamen = "childname";
    public static final String princn = "scprin";
    public static final String scemailn = "sce";
    public static final String scaddn = "sca";
    public static final String scphonen = "scphone";
    public static final String parentnamen = "parentname";
    public static final String classn = "class";
    public static final String emailn = "email";
    public static final String phnon = "phno";
    public static final String drivernamen = "driname";
    public static final String driveraddn = "driaddn";
    public static final String driveremailn = "driemail";
    public static final String driverphonen = "driphone";
    public static final String addn = "addn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        cname = (EditText) findViewById(R.id.childname);
        pname = (EditText) findViewById(R.id.pname);
        phno = (EditText) findViewById(R.id.phno);
        addr = (EditText) findViewById(R.id.addr);
        cls = (EditText) findViewById(R.id.clas);
        email = (EditText) findViewById(R.id.email);

    }
    public void onClickContinue(View view){
        if(check()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child(school).child("children");
            myRef.child(phno.getText().toString()).child("childname").setValue(cname.getText().toString());
            myRef.child(phno.getText().toString()).child("parentname").setValue(pname.getText().toString());
            myRef.child(phno.getText().toString()).child("class").setValue(cls.getText().toString());
            myRef.child(phno.getText().toString()).child("phno").setValue(phno.getText().toString());
            myRef.child(phno.getText().toString()).child("email").setValue(email.getText().toString());
            myRef.child(phno.getText().toString()).child("address").setValue(addr.getText().toString());
            final DatabaseReference imgstatus = database.getReference().child(school).child("children").child(phno.getText().toString()).child("imgstatus");
            imgstatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
//                    Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();
//                    String mi_v = map.get("mi_status");
//                    String fi_v = map.get("fi_status");
//                    String ci_v = map.get("ci_status");
//                    if(mi_v==null){
//                        imgstatus.child(phno.getText().toString()).child("imgstatus").child("mi_s").setValue("false");
//                    }
//                    if(fi_v==null){
//                        imgstatus.child(phno.getText().toString()).child("imgstatus").child("fi_s").setValue("false");
//                    }
//                    if(ci_v==null){
//                        imgstatus.child(phno.getText().toString()).child("imgstatus").child("ci_s").setValue("false");
//                    }
                    if(dataSnapshot.getValue()==null){
                        imgstatus.child("mi_s").setValue("false");
                        imgstatus.child("fi_s").setValue("false");
                        imgstatus.child("ci_s").setValue("false");

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });



            DatabaseReference driver = database.getReference().child(school).child("driver");
            DatabaseReference prin = database.getReference().child(school).child("prinicipal");
            DatabaseReference ad = database.getReference().child(school).child("address");
            DatabaseReference ema = database.getReference().child(school).child("email");
            DatabaseReference phn = database.getReference().child(school).child("phone");

            final SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(childnamen,cname.getText().toString());
            editor.putString(parentnamen,pname.getText().toString());
            editor.putString(classn,cls.getText().toString());
            editor.putString(phnon,phno.getText().toString());
            editor.putString(emailn,email.getText().toString());
            editor.putString(addn,addr.getText().toString());
            editor.putBoolean(openstatus, false);
            editor.commit();
            driver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();
                    String drivername = map.get("name");
                    String driveremail = map.get("email");
                    String driverphone = map.get("phone");
                    String driveraddress = map.get("address");
                    editor.putString(drivernamen,drivername);
                    editor.putString(driveraddn,driveraddress);
                    editor.putString(driveremailn,driveremail);
                    editor.putString(driverphonen,driverphone);
                    editor.commit();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
            prin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    editor.putString(princn,value);
                    editor.commit();
                    Log.d("d", "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("d", "Failed to read value.", error.toException());
                }
            });
            ad.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    editor.putString(scaddn,value);
                    editor.commit();

                    Log.d("d", "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("d", "Failed to read value.", error.toException());
                }
            });
            ema.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    editor.putString(scemailn,value);
                    editor.commit();

                    Log.d("d", "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("d", "Failed to read value.", error.toException());
                }
            });
            phn.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value1 = dataSnapshot.getValue(String.class);
                    editor.putString(scphonen,value1);
                    editor.commit();
                    Log.d("d", "Value is: " + value1);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("d", "Failed to read value.", error.toException());
                }
            });
            editor.commit();
            Intent i = new Intent(DetailsActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        }else {
            Toast.makeText(this, "Enter all details!", Toast.LENGTH_SHORT).show();
        }
    }
    public Boolean check(){
        if(cname.getText().toString().equals("")){
           return false;
        }
        if(pname.getText().toString().equals("")){
            return false;
        }
        if(phno.getText().toString().equals("")){
            return false;
        }
        if(addr.getText().toString().equals("")){
            return false;
        }
        if(cls.getText().toString().equals("")){
            return false;
        }
        if(email.getText().toString().equals("")){
            return false;
        }


        return true;
    }

}
