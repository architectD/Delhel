package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private TextView statusMessage;
    int count = 0;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "RouteList";
    public static boolean flag = false;

    public static LinkedList<EditText> editTexts = new LinkedList<>();
    ScrollView scrollView;
    LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_list);

        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        linearLayout =  (LinearLayout) scrollView.getChildAt(0);
        statusMessage = (TextView)findViewById(R.id.status_message);
    }

    public void onClickBarCode(View view){
        flag = false;
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    public void onClickStart(View view){
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);

                    EditText editText1 = (EditText) linearLayout.getChildAt(linearLayout.getChildCount() - 1);

                    EditText editText = new EditText(this);
                    editText1.setText(barcode.displayValue);
                    editText.setText("Добавить адрес");
                    editText.setBackgroundColor(Color.parseColor("#F2F2F2"));

                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(editText1.getWidth(),
                            editText1.getHeight());
                    parms.topMargin = 18;
                    parms.leftMargin = editText1.getLeft();

                    editText.setLayoutParams(parms);

                    linearLayout.addView(editText);//, editText1.getWidth(), editText1.getHeight());
                    editTexts.add(editText1);

                    if (!flag) {
                        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                        startActivityForResult(intent, RC_BARCODE_CAPTURE);
                    }

                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
