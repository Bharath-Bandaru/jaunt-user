package com.akhil.jauntparents;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;

import static com.akhil.jauntparents.DetailsActivity.addn;
import static com.akhil.jauntparents.DetailsActivity.childnamen;
import static com.akhil.jauntparents.DetailsActivity.classn;
import static com.akhil.jauntparents.DetailsActivity.emailn;
import static com.akhil.jauntparents.DetailsActivity.parentnamen;
import static com.akhil.jauntparents.DetailsActivity.phnon;
import static com.akhil.jauntparents.MainActivity.school;
import static com.akhil.jauntparents.SplashActivity.MyPREFERENCES;

/**
 * Created by Bharath on 14/01/17.
 */

public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    TextView kid,parent,calls,add,sect,mail;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    ImageView mi,fi,ci;
    private static final int MOTHER=1;
    private static final int CHILD=2;
    DatabaseReference imgstatus;
    private static final int FATHER=3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorage = FirebaseStorage.getInstance().getReference();
        progressDialog= new ProgressDialog(this);
        setContentView(R.layout.profile);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        kid = (TextView) findViewById(R.id.kid);
        parent = (TextView) findViewById(R.id.parent);
        calls = (TextView) findViewById(R.id.calls);
        add = (TextView) findViewById(R.id.proaddr);
        sect = (TextView) findViewById(R.id.sect);
        mail = (TextView) findViewById(R.id.mails);
        mi = (ImageView) findViewById(R.id.mother_image);
        fi = (ImageView) findViewById(R.id.father_image);
        ci = (ImageView) findViewById(R.id.child_image);
        kid.setText(sharedpreferences.getString(childnamen,"none"));
        parent.setText(sharedpreferences.getString(parentnamen,"none"));
        sect.setText(sharedpreferences.getString(classn,"none"));
        calls.setText(sharedpreferences.getString(phnon,"none"));
        mail.setText(sharedpreferences.getString(emailn,"none"));
        add.setText(sharedpreferences.getString(addn,"none"));
        mi.setImageResource(R.drawable.ic_loading);
        fi.setImageResource(R.drawable.ic_loading);
        ci.setImageResource(R.drawable.ic_loading);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        imgstatus = database.getReference().child(school).child("children").child(sharedpreferences.getString(phnon,"none")).child("imgstatus");
        imgstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String,String> map = (Map<String, String>) dataSnapshot.getValue();
                String mi_v = map.get("mi_s");
                String fi_v = map.get("fi_s");
                String ci_v = map.get("ci_s");
                if(mi_v.equals("true")){
                    String miurl = map.get("mi");
                    Picasso.with(ProfileActivity.this).load(miurl).fit().centerCrop().into(mi);
                }
                else{
                    mi.setImageResource(R.drawable.upload_image);

                }
                if(fi_v.equals("true")){
                    String fiurl = map.get("fi");
                    Picasso.with(ProfileActivity.this).load(fiurl).fit().centerCrop().into(fi);
                }else {
                    fi.setImageResource(R.drawable.upload_image);

                }
                if(ci_v.equals("true")){
                    String ciurl = map.get("ci");
                    Picasso.with(ProfileActivity.this).load(ciurl).fit().centerCrop().into(ci);
                }else {
                    ci.setImageResource(R.drawable.upload_image);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }
    public void onClickUpload1(View view){

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,MOTHER);
    }
    public void onClickUpload2(View view){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,FATHER);
    }
    public void onClickUpload3(View view){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,CHILD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MOTHER && resultCode==RESULT_OK){
            progressDialog.setMessage("uploading ... ");
            progressDialog.show();
            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri =  taskSnapshot.getDownloadUrl();
                    Picasso.with(ProfileActivity.this).load(downloaduri).fit().centerCrop().into(mi);
                    Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();
                    imgstatus.child("mi_s").setValue("true");
                    imgstatus.child("mi").setValue(downloaduri+"");
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "fail", Toast.LENGTH_SHORT).show();

                }
            });
        }
        if(requestCode==FATHER && resultCode==RESULT_OK){
            progressDialog.setMessage("uploading ... ");
            progressDialog.show();
            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri =  taskSnapshot.getDownloadUrl();
                    Picasso.with(ProfileActivity.this).load(downloaduri).fit().centerCrop().into(fi);
                    Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();
                    imgstatus.child("fi_s").setValue("true");
                    imgstatus.child("fi").setValue(downloaduri+"");
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "fail", Toast.LENGTH_SHORT).show();

                }
            });
        }if(requestCode==CHILD && resultCode==RESULT_OK){
            progressDialog.setMessage("uploading ... ");
            progressDialog.show();
            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri =  taskSnapshot.getDownloadUrl();
                    Picasso.with(ProfileActivity.this).load(downloaduri).fit().centerCrop().into(ci);
                    imgstatus.child("ci_s").setValue("true");
                    imgstatus.child("ci").setValue(downloaduri+"");
                    Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "fail", Toast.LENGTH_SHORT).show();

                }
            });
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
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;        }

        return super.onOptionsItemSelected(item);
    }
}
