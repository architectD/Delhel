package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.vision.barcode.Barcode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RouteList extends AppCompatActivity{
    private static final int RC_BARCODE_CAPTURE = 9001;
    public static boolean flag = false;
    public static double latitude, longitude;


    public static LinkedList<TextView> textViews;
    ScrollView scrollView;
    LinearLayout linearLayout;
    TextView address, tempField;
    public static String startAddress = "", town = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_list);

        tempField = new TextView(this);
        address = (TextView) findViewById(R.id.editText12);
        address.setText(startAddress);
        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        linearLayout =  (LinearLayout) scrollView.getChildAt(0);
        tempField = (TextView) linearLayout.getChildAt(0);
        tempField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlace();
            }
        });
        textViews = new LinkedList<>();
        QRcode.set.clear();
    }

    public void onClickBarCode(View view){
        flag = false;
        Intent intent = new Intent(this, QRcode.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    public void onClickStart(View view){
        if (textViews.size() > 0)
            startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null && !flag) {
                    Barcode barcode = data.getParcelableExtra("Barcode");

                    makeNextEnter(barcode.displayValue);

                    Intent intent = new Intent(this, QRcode.class);
                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                }
            }
        }
        else {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                try {
                    String[] addresses = place.getAddress().toString().split(",");
                    if (addresses.length >= 5)
                        makeNextEnter(addresses[2] + ", " + addresses[0] + " " + addresses[1]);
                } catch (Exception e){}
            }
        }
    }

    void makeNextEnter(String place){
        TextView topEditText = (TextView) linearLayout.getChildAt(linearLayout.getChildCount() - 1);

        String str = place.substring(0, place.indexOf(','));
        if (town.isEmpty())
            town = str;
        if (str.equals(town)) {
            topEditText.setText(place.substring(place.indexOf(',') + 2));

            TextView bottomEditText = new TextView(this);
            bottomEditText.setLayoutParams(topEditText.getLayoutParams());
            bottomEditText.setHint(topEditText.getHint());
            bottomEditText.setTextSize(18);
            bottomEditText.setHintTextColor(topEditText.getHintTextColors());
            bottomEditText.setTextColor(topEditText.getTextColors());
            bottomEditText.setBackgroundColor(((ColorDrawable) topEditText.getBackground()).getColor());
            bottomEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPlace();
                }
            });
            topEditText.setOnClickListener(null);

            linearLayout.addView(bottomEditText);
            textViews.add(topEditText);
        }
    }

    private void clickPlace() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(RouteList.this);
            startActivityForResult(intent, 15);
        } catch (Exception e) {
            Toast toast = Toast.makeText(RouteList.this, "Требуется скачать Серивисы Google play.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
