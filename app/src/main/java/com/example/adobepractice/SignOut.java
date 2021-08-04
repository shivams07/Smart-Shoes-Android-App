package com.example.adobepractice;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class SignOut extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavigationView nv;
    private Toolbar t;
    GoogleSignInClient mGoogleSignInClient;
    ImageView photoIV;
    TextView nameTV,emailTV;
    Button sign_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);
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
        sign_out = findViewById(R.id.log_out);
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
                        Intent i=new Intent(SignOut.this,Contact.class);
                        startActivity(i);
                        break;
                    case R.id.connect:
                        Intent i2=new Intent(SignOut.this,BluetoothScreen.class);
                        startActivity(i2);
                        break;
                    case R.id.summary:
                        Intent intent=new Intent(SignOut.this,Summary.class);
                        startActivity(intent);
                        break;
                    case R.id.charge:
                        Intent intent1=new Intent(SignOut.this,Charge.class);
                        startActivity(intent1);
                        break;
                    case R.id.Goalinfo:
                        Intent intent2=new Intent(SignOut.this,Goal.class);
                        startActivity(intent2);
                        break;
                    case R.id.signout:
                        Intent intent3=new Intent(SignOut.this,SignOut.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intent4=new Intent(SignOut.this,NavDrawer.class);
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

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SignOut.this);
        if (acct != null) {

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
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignOut.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignOut.this, Login_Screen.class));
                        finish();
                    }
                });
    }
}
