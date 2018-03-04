package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Route extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);
    }

    public void onClickNext(View view){
        try {
            startActivity(new Intent(this, RouteList.class));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
