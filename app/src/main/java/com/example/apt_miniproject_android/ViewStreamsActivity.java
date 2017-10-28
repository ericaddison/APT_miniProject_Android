package com.example.apt_miniproject_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerResponseAction;
import com.example.apt_miniproject_android.model.StreamIdArray;
import com.example.apt_miniproject_android.model.StreamInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

public class ViewStreamsActivity extends BaseActivity {

    private GridView gridview;
    private StreamGridViewAdapter adapter;
    private ServerCommunicator comm;
    private ServerResponseAction fillGridServerAction;
    private boolean showingAll;
    private Button subButton;
    private static final String TAG = ViewStreamsActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
                Intent i = new Intent(v.getContext(), ViewAStreamActivity.class);
                i.putExtra("streamID", id);
                startActivity(i);
            }
        });

        // set "nearby" click behavior
        View nearbyView = (View) findViewById(R.id.nearby_image);
        nearbyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), FindNearbyActivity.class);
                startActivity(i);
            }
        });

        // set search text field behavior
        final EditText searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                showSearchStreams(textView.getText().toString());
                return false;
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });

        // set subscribed button behavior
        subButton = (Button) findViewById(R.id.button_viewstreams_sub);
        subButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(showingAll && getSignInAccount()!=null) {
                    showSubscribedStreams();
                    subButton.setText(getString(R.string.all_streams_button_text));
                } else {
                    showAllStreams();
                    subButton.setText(getString(R.string.sub_streams_button_text));
                }
            }
        });

    }


    private void setSubButtonToAllStreams(){
        subButton.setText(getString(R.string.all_streams_button_text));
        showingAll = false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        showAllStreams();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        subButton.setEnabled(getSignInAccount()!=null);
    }

    private void showSubscribedStreams(){
        adapter.clear();
        comm.requestSubscribedStreamsInfoData(getSignInAccount().getIdToken(), fillGridServerAction);
        showingAll = false;
    }


    private void showAllStreams(){
        adapter.clear();
        comm.requestAllStreamInfoData(fillGridServerAction);
        showingAll = true;
    }

    private void showSearchStreams(String searchTerm){

        if(searchTerm.equals(""))
            return;

        // get stream data
        comm.requestSearchStreamsData(searchTerm, new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                final Gson gson = new GsonBuilder().create();
                StreamIdArray streamIDs = gson.fromJson(response, StreamIdArray.class);
                comm.requestStreamInfoData(Arrays.asList(streamIDs.getStreamID()), new ServerResponseAction() {
                    @Override
                    public void handleResponse(String response) {
                        StreamInfo[] streams = gson.fromJson(response, StreamInfo[].class);
                        adapter.clear();
                        for (StreamInfo stream : streams)
                            adapter.addStreamInfo(stream);
                        adapter.notifyDataSetChanged();
                        try {
                            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                });
            }
        });

        setSubButtonToAllStreams();

    }


}


