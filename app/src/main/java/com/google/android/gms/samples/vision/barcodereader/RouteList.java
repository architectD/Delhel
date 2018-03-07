package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.LinkedList;

public class RouteList extends AppCompatActivity{
    private static final int RC_BARCODE_CAPTURE = 9001;
    public static boolean flag = false;
    public static double latitude, longitude;

    public static LinkedList<EditText> editTexts = new LinkedList<>();
    ScrollView scrollView;
    LinearLayout linearLayout;
    TextView address;
    public static String startAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_list);

        address = (TextView)findViewById(R.id.editText12);
        address.setText(startAddress);
        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        linearLayout =  (LinearLayout) scrollView.getChildAt(0);
    }

    public void onClickBarCode(View view){
        flag = false;
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    public void onClickStart(View view){
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null && !flag) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    EditText topEditText = (EditText) linearLayout.getChildAt(linearLayout.getChildCount() - 1);
                    topEditText.setText(barcode.displayValue);

                    EditText bottomEditText = new EditText(this);
                    bottomEditText.setLayoutParams(topEditText.getLayoutParams());
                    bottomEditText.setHint(bottomEditText.getHint());
                    bottomEditText.setImeOptions(topEditText.getImeOptions());
                    bottomEditText.setBackgroundColor(bottomEditText.getDrawingCacheBackgroundColor());

                    topEditText.setInputType(InputType.TYPE_NULL);

                    linearLayout.addView(bottomEditText);
                    editTexts.add(topEditText);

                    Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
