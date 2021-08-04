package com.example.adobepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;
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

public class Goal extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavigationView nv;
    private Toolbar t;
    DatabaseReference duser,d;
    GoogleSignInClient mGoogleSignInClient;
    ImageView photoIV;
    TextView nameTV,emailTV,goal_n,tex,goal_s;
    ArcProgress a;
    public String m_Text="Empty";
    String personName;
    steps s;
    user n,u;
    String Steps,gn="";Integer i,j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Steps="";gn="";
        setContentView(R.layout.activity_goal);
        t=findViewById(R.id.tool);
        tex=findViewById(R.id.goal_update);
        setSupportActionBar(t);
        goal_s=findViewById(R.id.goal_succeeded);
        nv=findViewById(R.id.nv);
        goal_n=findViewById(R.id.textView9);
        a=findViewById(R.id.arc_progress);
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
                        Intent i=new Intent(Goal.this,Contact.class);
                        startActivity(i);
                        break;
                    case R.id.connect:
                        Intent i2=new Intent(Goal.this,BluetoothScreen.class);
                        startActivity(i2);
                        break;
                    case R.id.summary:
                        Intent intent=new Intent(Goal.this,Summary.class);
                        startActivity(intent);
                        break;
                    case R.id.charge:
                        Intent intent1=new Intent(Goal.this,Charge.class);
                        startActivity(intent1);
                        break;
                    case R.id.Goalinfo:
                        Intent intent2=new Intent(Goal.this,Goal.class);
                        startActivity(intent2);
                        break;
                    case R.id.signout:
                        Intent intent3=new Intent(Goal.this,SignOut.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intent4=new Intent(Goal.this,NavDrawer.class);
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

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Goal.this);
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
        duser= FirebaseDatabase.getInstance().getReference("goal").child(personName);
        d= FirebaseDatabase.getInstance().getReference("user").child(personName);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        final String Date=formattedDate;
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals(Date)){
                        u=ds.getValue(user.class);
                        tex.setText("You have walked "+u.getSteps().toString()+" steps");
                        gn=u.getSteps().toString();
                    }
                    else {


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        duser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Steps=ds.getValue(String.class);
                    if(!Steps.isEmpty() && !gn.isEmpty()){
                        goal_n.setText("Your Goal is "+Steps);

                        Float k=((Float.valueOf(gn)/Float.valueOf(Steps))*100);
                        i=Math.round(k);
                        if(i<100){
                            a.setProgress(i);}
                        else
                        {
                            a.setProgress(100);
                        }
                        if (Integer.valueOf(Steps)<Integer.valueOf(gn))
                        {
                            goal_s.setText("You have Completed your Goal");
                            goal_s.setBackgroundColor(Color.LTGRAY);
                            goal_s.setTextColor(Color.RED);
                        }
                        else {
                            goal_s.setText("You have not Completed your Goal");
                            goal_s.setTextColor(Color.WHITE);
                            goal_s.setBackgroundColor(Color.parseColor("#E72872"));
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Goal is not Set",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
public void setGoal(View v){
        takeInput();
    if(!TextUtils.isEmpty(m_Text))
    {

    }else{
        //
    }
}

    public void takeInput(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Goal");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().isEmpty()) {
                    if (input.getText().toString().matches("[0-9]+") && input.getText().toString().length()<6) {
                        m_Text = input.getText().toString();
                        s = new steps(m_Text);
                        duser.setValue(s);
                        Toast.makeText(getApplicationContext(), "Goal Saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Goal should be in Number Format and less than 5 digits ",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter a number ",Toast.LENGTH_LONG).show();
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
