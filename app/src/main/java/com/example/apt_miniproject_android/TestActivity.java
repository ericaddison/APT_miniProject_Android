package com.example.apt_miniproject_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.apt_miniproject_android.backend.DefaultServerErrorAction;
import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerErrorAction;
import com.example.apt_miniproject_android.backend.ServerResponseAction;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRequest(view);
            }
        });



    }


    private void doRequest(View view){

        ServerCommunicator comm = new ServerCommunicator(view);

        final TextView text = (TextView) findViewById(R.id.textView);
        text.setText("loading...");

        ServerResponseAction callback = new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                text.setText(response);
            }
        };

        ServerErrorAction errorAction = new DefaultServerErrorAction(view){
            @Override
            public void handleError(VolleyError error) {
                super.handleError(error);
                text.setText("Error reaching server...");
            }
        };

        comm.setErrorAction(errorAction).requestViewStreamData("5631383682678784", 1, 10, callback);
    }

}
