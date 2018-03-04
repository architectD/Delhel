package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
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
        try {
            RouteList.startAddress = "Санкт-Петербург, Большой Сампсониевский 61";
            startActivity(new Intent(this, RouteList.class));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
