package com.example.adobepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contact extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavigationView nv;
    private Toolbar t;
    GoogleSignInClient mGoogleSignInClient;
    ImageView photoIV;
    TextView nameTV, emailTV,trust;
    TextView t1,t2,msg;
    int PERMISSION_ID = 44;
    Button b1;
    String lat,longi;
    String personName,m_Text,ctype;
    ArrayList<String> ar = new ArrayList<String>();
    ListView listv;
    ArrayList<String> ulist;
    ArrayAdapter<String> uadapter;
    DatabaseReference duser,d;
    FusedLocationProviderClient mFusedLocationClient;
    trusted_contacts u;
    @SuppressLint({"NewApi", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        t=findViewById(R.id.tool);
        setSupportActionBar(t);
        nv=findViewById(R.id.nv);
        ulist=new ArrayList<>();
        msg=findViewById(R.id.trustedcontactslist);
        listv=findViewById(R.id.list1);
        t1=findViewById(R.id.textView5);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        t2=findViewById(R.id.trustedcontactslist);
        ActionBar act=getSupportActionBar();
        act.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        act.setDisplayHomeAsUpEnabled(true);
        act.setHomeButtonEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        View hView =  navigationView.getHeaderView(0);
        photoIV=hView.findViewById(R.id.acc_pic);
        nameTV=hView.findViewById(R.id.gname);
        emailTV=hView.findViewById(R.id.gemail);
        u=new trusted_contacts();
        uadapter=new ArrayAdapter<String>(this,R.layout.list_contacts,R.id.contactnp,ulist);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        b1=findViewById(R.id.button3);
        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Contact.this);
        if (acct != null) {

            Uri personPhoto = acct.getPhotoUrl();
            personName = acct.getId();
            String personEmail = acct.getEmail();
            nameTV.setTextColor(Color.parseColor("#ffffff"));
            emailTV.setTextColor(Color.parseColor("#ffffff"));
            nameTV.setText(acct.getDisplayName().toString());
            emailTV.setText(personEmail);
            if(personPhoto==null)
            {
                photoIV.setImageResource(R.drawable.avatar);
            }
            else {
                Glide.with(this).load(personPhoto).into(photoIV);
            }
        }
        duser= FirebaseDatabase.getInstance().getReference("contacts").child(personName);
        d= FirebaseDatabase.getInstance().getReference("contacts");
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.Trusted:
                        Intent i=new Intent(Contact.this,Contact.class);
                        startActivity(i);
                        break;
                    case R.id.connect:
                        Intent i2=new Intent(Contact.this,BluetoothScreen.class);
                        startActivity(i2);
                        break;
                    case R.id.summary:
                        Intent intent=new Intent(Contact.this,Summary.class);
                        startActivity(intent);
                        break;
                    case R.id.charge:
                        Intent intent1=new Intent(Contact.this,Charge.class);
                        startActivity(intent1);
                        break;
                    case R.id.Goalinfo:
                        Intent intent2=new Intent(Contact.this,Goal.class);
                        startActivity(intent2);
                        break;
                    case R.id.signout:
                        Intent intent3=new Intent(Contact.this,SignOut.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intent4=new Intent(Contact.this,NavDrawer.class);
                        startActivity(intent4);
                        break;
                }

                return true;

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        duser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ulist.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String prim=  ds.getValue(String.class);
                    ar.add(prim);
                    ulist.add(prim);
                }
                listv.setAdapter(uadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat=String.valueOf(location.getLatitude());
                                    longi=String.valueOf(location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        return false;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationCallback mLocationCallback=new LocationCallback();
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS},
                PERMISSION_ID
        );
    }
    public void sendSms(android.view.View v1) {
            PendingIntent sentIntent = null, deliveryIntent = null;
            SmsManager s = SmsManager.getDefault();
            if(ar.size()!=0){
            for (String a : ar) {
                String scAddress = null;
                String uri = "http://maps.google.com/maps?q=" + lat + "," + longi + "&ll=" + lat + "," + longi + "&z=17";
                s.sendTextMessage(a, scAddress, uri, sentIntent, deliveryIntent);
                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
            }}
            else {
                Toast.makeText(getApplicationContext(), "No Contacts", Toast.LENGTH_LONG).show();
            }
        }


    public void addContact(View v1){
        View checkBoxView = View.inflate(this, R.layout.checkbox_alert, null);
        final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkBox);
        final EditText input=checkBoxView.findViewById(R.id.editText1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a Contact");
        builder.setView(checkBoxView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(m_Text.length()==10 && m_Text.matches("[0-9]+")) {
                    if(checkBox.isChecked()==true) {
                        Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                        String sec;
                        if (ar.size() < 1) {
                            sec = "Empty";
                        } else {
                            sec = ar.get(1);
                        }
                        u = new trusted_contacts(m_Text, sec);
                        duser.setValue(u);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                        String sec;
                        if (ar.size() < 1) {
                            sec = "Empty";
                        } else {
                            sec = ar.get(0);
                        }
                        u = new trusted_contacts(sec,m_Text);
                        duser.setValue(u);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter a 10 digit Number and in Number Format", Toast.LENGTH_LONG).show();
                }
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_LONG).show();
            }
        });

        builder.show();

    }

}
