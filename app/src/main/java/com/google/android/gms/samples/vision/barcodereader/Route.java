package com.google.android.gms.samples.vision.barcodereader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Route extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);
    }

    public void onClickNext(View view){
        try {
            RouteList.startAddress = ((EditText)findViewById(R.id.editText2)).getText().toString();
            startActivity(new Intent(this, RouteList.class));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClickGps(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            RouteList.startAddress = "Моё местоположение";
            startActivity(new Intent(this, RouteList.class));
        } else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

    }


}
