package com.example.adobepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramijemli.percentagechartview.PercentageChartView;

public class Charge extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavigationView nv;
    private Toolbar t; String personName;
    DatabaseReference duser;
    GoogleSignInClient mGoogleSignInClient;
    ImageView photoIV;
    TextView nameTV,emailTV,steps_rem;
    WebView webView;
    private int steps,level;
    private TextView battery_level;
    private PercentageChartView p;
    private BroadcastReceiver mBatInfo=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            float s=(float)level;
            p.setProgress(s,true);
            steps=((100-level)*30000)/100;
            steps_rem.setText("You need to walk "+String.valueOf(steps)+" Steps ");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        t=findViewById(R.id.tool);
        setSupportActionBar(t);
        nv=findViewById(R.id.nv);
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
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.Trusted:
                        Intent i=new Intent(Charge.this,Contact.class);
                        startActivity(i);
                        break;
                    case R.id.connect:
                        Intent i2=new Intent(Charge.this,BluetoothScreen.class);
                        startActivity(i2);
                        break;
                    case R.id.summary:
                        Intent intent=new Intent(Charge.this,Summary.class);
                        startActivity(intent);
                        break;
                    case R.id.charge:
                        Intent intent1=new Intent(Charge.this,Charge.class);
                        startActivity(intent1);
                        break;
                    case R.id.Goalinfo:
                        Intent intent2=new Intent(Charge.this,Goal.class);
                        startActivity(intent2);
                        break;
                    case R.id.signout:
                        Intent intent3=new Intent(Charge.this,SignOut.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intent4=new Intent(Charge.this,NavDrawer.class);
                        startActivity(intent4);
                        break;
                }

                return true;

            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Charge.this);
        if (acct != null) {
            personName = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            nameTV.setTextColor(Color.parseColor("#ffffff"));
            emailTV.setTextColor(Color.parseColor("#ffffff"));
            nameTV.setText(personName);
            emailTV.setText(personEmail);
            if(personPhoto==null)
            {
                photoIV.setImageResource(R.drawable.avatar);
            }
            else {
                Glide.with(this).load(personPhoto).into(photoIV);
            }
        }
        battery_level=findViewById(R.id.textView6);
        p=findViewById(R.id.view_id);
        steps_rem=findViewById(R.id.textView6);
        this.registerReceiver(this.mBatInfo,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        duser= FirebaseDatabase.getInstance().getReference("goal").child(personName);
        webView=findViewById(R.id.webView);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String file="file:android_asset/walkinge.gif";
        webView.loadUrl(file);
    }
}
