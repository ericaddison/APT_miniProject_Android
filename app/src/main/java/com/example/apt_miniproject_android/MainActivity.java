package com.example.apt_miniproject_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ServerCommunicator comm = new ServerCommunicator(this);

        final TextView text = (TextView) findViewById(R.id.textView);
        text.setText("loading...");

        ServerResponseAction callback = new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                text.setText(response);
            }
        };

        comm.requestViewStreamData("5631383682678784", 1, 10, callback);

    }
}
