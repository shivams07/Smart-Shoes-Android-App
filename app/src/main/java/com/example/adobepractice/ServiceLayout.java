package com.example.adobepractice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ServiceLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_layout);
    }
    public void startService(View v){
        Intent i=new Intent(this,ServiceClass.class);
        startService(i);
    }
}
