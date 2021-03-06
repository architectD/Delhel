package com.google.android.gms.samples.vision.barcodereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;

public class MapActivity extends AppCompatActivity {

    //ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar3);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        HashSet<String> strings = new HashSet<>();
        for (TextView textView: RouteList.textViews){
            strings.add(RouteList.town + ", " + textView.getText().toString());
        }

        try {
            InputStream is = getAssets().open("ymap.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder htmlText = new StringBuilder();

            while (br.ready()){
                String st = br.readLine();
                if (st.contains("<!--массив данных-->")){
                    StringBuilder sb = new StringBuilder("var points = [");
                    if (RouteList.startAddress.equals("Моё местоположение"))
                        sb.append('[').append(RouteList.latitude).append(',').append(RouteList.longitude).append("],");
                    else
                        sb.append('"').append(RouteList.startAddress).append("\",");
                    for (String s: strings){
                        sb.append('"').append(s).append('"').append(',');
                    }
                    sb.replace(sb.length() - 1, sb.length(), "]");
                    sb.append(';');
                    st = sb.toString();
                }
                st += '\n';
                htmlText.append(st);
            }

            myWebView.loadDataWithBaseURL(
                    "http://ru.yandex.api.yandexmapswebviewexample.ymapapp",
                    htmlText.toString(),
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
