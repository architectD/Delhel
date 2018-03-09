package com.google.android.gms.samples.vision.barcodereader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;
import java.util.HashSet;

public class QRcode extends AppCompatActivity {

    SurfaceView cameraPreview;
    BarcodeDetector detector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    public static HashSet<String> set = new HashSet<>();

    Barcode lastStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
        detector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        cameraSource = new CameraSource.Builder(this, detector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(width, (int)(width * 0.75))
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(QRcode.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    ActivityCompat.requestPermissions(
                            QRcode.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            RequestCameraPermissionID);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0){
                    lastStr = qrcodes.valueAt(0);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(QRcode.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED)
                        try {
                            cameraSource.start(cameraPreview.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
            break;
        }
    }

    public void ScreenOnClick(View view){
        if (lastStr != null && !set.contains(lastStr.displayValue)) {
            Toast toast = Toast.makeText(this, "Добавлено", Toast.LENGTH_SHORT);
            toast.show();
            set.add(lastStr.displayValue);
            Intent data = new Intent();
            data.putExtra("Barcode", lastStr);
            setResult(CommonStatusCodes.SUCCESS, data);
            finish();
        } else{
            Toast toast = Toast.makeText(this, "Попробуйте снова", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void backOnClick(View view){
        RouteList.flag = true;
        setResult(CommonStatusCodes.SUCCESS, null);
        finish();
    }
}
