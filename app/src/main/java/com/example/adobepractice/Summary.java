package com.example.adobepractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Summary extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavigationView nv;
    private Toolbar t;
    DatabaseReference duser;
    ListView listv;
    ArrayList<String> ulist;
    ArrayAdapter<String> uadapter;
    user u;
    GoogleSignInClient mGoogleSignInClient;
    ImageView photoIV;
    TextView nameTV,emailTV;
    String personName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        t=findViewById(R.id.tool);
        setSupportActionBar(t);
        nv=findViewById(R.id.nv);
        listv=findViewById(R.id.list);
        u=new user();
        ulist=new ArrayList<>();
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        ActionBar act=getSupportActionBar();
        act.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        act.setDisplayHomeAsUpEnabled(true);
        act.setHomeButtonEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        View hView =  navigationView.getHeaderView(0);
        photoIV=hView.findViewById(R.id.acc_pic);
        nameTV=hView.findViewById(R.id.gname);
        emailTV=hView.findViewById(R.id.gemail);
        uadapter=new ArrayAdapter<String>(this,R.layout.list_item,R.id.name,ulist);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Summary.this);
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
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        duser= FirebaseDatabase.getInstance().getReference("user").child(personName);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.Trusted:
                        Intent i=new Intent(Summary.this,Contact.class);
                        startActivity(i);
                        break;
                    case R.id.connect:
                        Intent i2=new Intent(Summary.this,BluetoothScreen.class);
                        startActivity(i2);
                        break;
                    case R.id.summary:
                        Intent intent=new Intent(Summary.this,Summary.class);
                        startActivity(intent);
                        break;
                    case R.id.charge:
                        Intent intent1=new Intent(Summary.this,Charge.class);
                        startActivity(intent1);
                        break;
                    case R.id.Goalinfo:
                        Intent intent2=new Intent(Summary.this,Goal.class);
                        startActivity(intent2);
                        break;
                    case R.id.signout:
                        Intent intent3=new Intent(Summary.this,SignOut.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intent4=new Intent(Summary.this,NavDrawer.class);
                        startActivity(intent4);
                        break;
                }

                return true;

            }
        });
        duser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    u =ds.getValue(user.class);
                    ulist.add("Date : " + u.getName()+" \nSteps : "+u.getSteps()+ "\nCalories : "+u.getCals());
                }
                listv.setAdapter(uadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
