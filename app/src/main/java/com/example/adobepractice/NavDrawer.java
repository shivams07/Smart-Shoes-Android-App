package com.example.adobepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class NavDrawer extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavigationView nv;
    private Toolbar t;
    Button b;
    DatabaseReference duser;
    GoogleSignInClient mGoogleSignInClient;
    ImageView photoIV;
    TextView nameTV,emailTV,stepsTV,caloriesTV,timeTV;
    String personName,steps,calories;
    user u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        t=findViewById(R.id.tool);
        setSupportActionBar(t);
        nv=findViewById(R.id.nv);
        timeTV=findViewById(R.id.time);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        stepsTV=findViewById(R.id.textView2);
        caloriesTV=findViewById(R.id.goal);
        steps=stepsTV.getText().toString();
        calories=caloriesTV.getText().toString();
        ActionBar act=getSupportActionBar();
        act.setTitle("hello");
        act.setDisplayHomeAsUpEnabled(true);
        act.setHomeButtonEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        View hView =  navigationView.getHeaderView(0);
        photoIV=hView.findViewById(R.id.acc_pic);
        nameTV=hView.findViewById(R.id.gname);
        emailTV=hView.findViewById(R.id.gemail);
        stepsTV.setText("0");
        caloriesTV.setText("0");
        timeTV.setText("0");
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.Trusted:
                        Intent i=new Intent(NavDrawer.this,Contact.class);
                        startActivity(i);
                        break;
                    case R.id.connect:
                        Intent i2=new Intent(NavDrawer.this,BluetoothScreen.class);
                        startActivity(i2);
                        break;
                    case R.id.summary:
                        Intent intent=new Intent(NavDrawer.this,Summary.class);
                        startActivity(intent);
                        break;
                    case R.id.charge:
                        Intent intent1=new Intent(NavDrawer.this,Charge.class);
                        startActivity(intent1);
                        break;
                    case R.id.Goalinfo:
                        Intent intent2=new Intent(NavDrawer.this,Goal.class);
                        startActivity(intent2);
                        break;
                    case R.id.signout:
                        Intent intent3=new Intent(NavDrawer.this,SignOut.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intent4=new Intent(NavDrawer.this,NavDrawer.class);
                        startActivity(intent4);
                        break;
                }

                return true;

            }
        });
        b=findViewById(R.id.sync);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(NavDrawer.this);
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
        duser= FirebaseDatabase.getInstance().getReference("user").child(personName);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        final String Date=formattedDate;
        duser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals(Date)){
                        u=ds.getValue(user.class);
                        stepsTV.setText(u.getCals().toString());
                        caloriesTV.setText(u.getSteps().toString());
                        String s=String.valueOf(Integer.valueOf(u.getSteps())/60);
                        timeTV.setText(s);
                    }
                    else {
                        /*stepsTV.setText("0");
                        caloriesTV.setText("0");
                        timeTV.setText("0");*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*private void getuser()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        String Date=formattedDate;
        TextView t=findViewById(R.id.goal);
        String stepsi=t.getText().toString();
        String calsi=calories;
        if(!TextUtils.isEmpty(Date))
        {
            user u=new user(Date,stepsi,calsi);
            duser.child(formattedDate).setValue(u);

            Toast.makeText(this,"Synchronizing Data",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"You should enter text",Toast.LENGTH_LONG).show();
        }
    }*/
    public void onSync(View v){
        Intent i=new Intent(getApplicationContext(),BluetoothScreen.class);
        startActivity(i);
    }
}
