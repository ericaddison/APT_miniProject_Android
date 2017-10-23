package com.example.apt_miniproject_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerResponseAction;
import com.example.apt_miniproject_android.model.StreamInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ViewStreamsActivity extends AppCompatActivity {

    private GridView gridview;
    private StreamGridViewAdapter adapter;
    private ServerCommunicator comm;
    private ServerResponseAction fillGridServerAction;
    private boolean showingAll;
    private Button subButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        comm = new ServerCommunicator(findViewById(android.R.id.content));
        fillGridServerAction = new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                Gson gson = new GsonBuilder().create();
                StreamInfo[] streams = gson.fromJson(response, StreamInfo[].class);
                for (StreamInfo stream : streams)
                    adapter.addStreamInfo(stream);
                adapter.notifyDataSetChanged();
            }
        };

        // set gridview adapter and click behavior
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new StreamGridViewAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ViewStreamsActivity.this, "Stream " + adapter.getItem(position).getId(),
                        Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(v.getContext(), ViewStreamActivity.class);
                //i.putExtra(adapter.getItem(position).getId());
                //startActivity(i);
            }
        });

        // set "nearby" click behavior
        View nearbyView = (View) findViewById(R.id.nearby_image);
        nearbyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewStreamsActivity.this, "Go to nearby streams page...",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), FindNearbyActivity.class);
                startActivity(i);
            }
        });

        // set search button behavior
        Button searchButton = (Button) findViewById(R.id.button_viewstreams_search);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewStreamsActivity.this, "Search for streams...",
                        Toast.LENGTH_SHORT).show();
                showSubscribedStreams();
            }
        });

        // set subscribed button behavior
        subButton = (Button) findViewById(R.id.button_viewstreams_sub);
        subButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(showingAll) {
                    showSubscribedStreams();
                    subButton.setText(getString(R.string.all_streams_button_text));
                } else {
                    showAllStreams();
                    subButton.setText(getString(R.string.sub_streams_button_text));
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        showAllStreams();
    }


    private void showSubscribedStreams(){
        adapter.clear();
        //comm.requestSubscribedStreamsInfoData(fillGridServerAction);
        showingAll = false;
    }


    private void showAllStreams(){
        comm.requestAllStreamInfoData(fillGridServerAction);
        showingAll = true;
    }
}


