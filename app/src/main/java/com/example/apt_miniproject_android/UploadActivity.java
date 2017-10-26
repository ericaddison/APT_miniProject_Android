package com.example.apt_miniproject_android;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apt_miniproject_android.backend.DefaultServerErrorAction;
import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerErrorAction;
import com.example.apt_miniproject_android.backend.ServerResponseAction;
import com.example.apt_miniproject_android.model.StreamInfo;
import com.example.apt_miniproject_android.model.StreamItemInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends BaseActivity {
    private static final String TAG = UploadActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private String picturePath = "NULL";
    private long streamID = new Long(0L);
    private String streamName = "NULL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Log.v(TAG, "ON CREATE!");

        // Set text hint
        EditText uploadHint;
        uploadHint = (EditText) findViewById(R.id.uploadEditText);
        uploadHint.setHint("Add a message and/or tags");
        uploadHint.setGravity(Gravity.TOP);

        // This line is used for testing a specific streamID
        //streamID = new Long(5631383682678784L);


        // Test to see if intent extra was passed - then get streamID from intent
        if (getIntent().getExtras() != null) {
            streamID = getIntent().getExtras().getLong("streamID");
            Log.v("INTENT INFO: ", Long.toString(streamID));

            // get name of stream from Server

            ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));

            comm.requestStreamItemInfoData(streamID, new ServerResponseAction() {
                @Override
                public void handleResponse(String response) {
                    Gson gson = new GsonBuilder().create();
                    StreamItemInfo[] streamItems = gson.fromJson(response, StreamItemInfo[].class);
                    for (StreamItemInfo item : streamItems)
                        streamName = item.getStreamName();
                    Log.v("stream name: ", streamName);

                    // Set stream name textbox
                    EditText streamNameTextBox = (EditText) findViewById(R.id.streamName);
                    streamNameTextBox.setEnabled(false);
                    streamNameTextBox.setText("Stream Name: " + streamName);
                }
            });

            // Do nothing if streamID was not passed along with intent.
        } else {
            Log.e(TAG, "Please specify \"streamID\" from calling Activity with intent.putExtra()");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "ON DESTROY!");
    }


    // BUTTON LISTENERS

    //TODO upload image button
    public void uploadImage(View view) {
        EditText uploadEditText = (EditText) findViewById(R.id.uploadEditText);
        String content = uploadEditText.getText().toString();
        Log.v("UploadText", content);
        Log.d("Picture path: ", this.picturePath);
    }

    //Selects a image from a library
    public void chooseFromLibrary(View view) {
        Log.d(TAG, "Choosing image from library!");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //start camera activity
    public void useCamera(View view) {
        Log.d(TAG, "CameraButton Pressed!");

        // Go to Camera Activity
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);

    }

    // This is called after the "chooseFromLibrary" button is pressed.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Update Image Preview
            // START
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.uploadPreview);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // END


            // Get absolute file path from content URI
            // START
            this.picturePath = uri.getPath();
            // END

            Log.d(TAG, "PICTURE PATH IS" + this.picturePath);
        }

    }


}
