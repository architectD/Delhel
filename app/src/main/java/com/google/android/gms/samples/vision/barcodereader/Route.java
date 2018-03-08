package com.google.android.gms.samples.vision.barcodereader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class Route extends AppCompatActivity {

    TextView enterField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);
        enterField = findViewById(R.id.geoAddress);
    }

    public void onClickEnterField(View view){
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            Toast toast = Toast.makeText(this, "Требуется скачать Серивисы Google play.", Toast.LENGTH_LONG);
            toast.show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast toast = Toast.makeText(this, "Требуется скачать Серивисы Google play.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                try {
                    String[] addresses = place.getAddress().toString().split(",");
                    enterField.setText(addresses[2] + ", " + addresses[0].split(". ")[1] + " " + addresses[1]);
                } catch (Exception e){}
            }
        }
    }

    public void onClickNext(View view){
        try {
            RouteList.startAddress = ((TextView)findViewById(R.id.geoAddress)).getText().toString();
            startActivity(new Intent(this, RouteList.class));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClickGps(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Acquire a reference to the system Location Manager
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    RouteList.latitude = location.getLatitude();
                    RouteList.longitude = location.getLongitude();

                    RouteList.startAddress = "Моё местоположение";
                    startActivity(new Intent(Route.this, RouteList.class));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
        } else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

    }



}
