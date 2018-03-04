package com.google.android.gms.samples.vision.barcodereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.LinkedList;

/**
 * Created by krikyn on 03.03.2018.
 */

public class Orders extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        WebView myWebView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        LinkedList<String> strings = new LinkedList<>();

        strings.add("Санкт-Петербург, Большой Сампсониевский 61");
        strings.add("Санкт-Петербург, Литейный проcпект 57");
        strings.add("Санкт-Петербург, Загородный проспект 15");
        strings.add("Санкт-Петербург, Звенигородская 5");
        strings.add("Санкт-Петербург, Марата 57");

        try {
            InputStream is = getAssets().open("ymap.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder htmlText = new StringBuilder();

            while (br.ready()){
                String st = br.readLine();
                if (st.contains("<!--массив данных-->")){
                    StringBuilder sb = new StringBuilder("var points = [");
                    for (String s: strings){
                        sb.append('"').append(s).append('"');
                        sb.append(',');
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
