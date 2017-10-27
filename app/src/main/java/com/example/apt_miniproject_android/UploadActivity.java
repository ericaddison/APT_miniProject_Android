package com.example.apt_miniproject_android;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerResponseAction;
import com.example.apt_miniproject_android.model.StreamItemInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UploadActivity extends BaseActivity {
    private static final String TAG = UploadActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private String streamID = "0";
    private String streamName = "NULL";
    private Uri picturePath;


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
            streamID = getIntent().getExtras().getString("streamID");
            streamName = getIntent().getExtras().getString("streamName");
            Log.v("UPLOAD", "INTENT INFO: "+ streamID);

            // get name of stream from Server
            ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));

            comm.requestStreamItemInfoData(Long.parseLong(streamID), new ServerResponseAction() {
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
        Log.d("Picture path: ", picturePath.toString());

        // get Upload URL
        ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));

        comm.requestUploadURL(new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                // Get unique upload URL
                String uploadURL = response;
                Log.v("URL: ", uploadURL);

                // MultiPart Form Post
                try {
                    // Turn picture path into byte array
                    byte[] data = null;
                    try {
                        ContentResolver cr = getBaseContext().getContentResolver();
                        InputStream inputStream = cr.openInputStream(picturePath);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        data = baos.toByteArray();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // print out byte array
                    Log.v("DATA: ", new String(data));

                    //TODO CREATE POST


                } catch (Exception e) {
                    Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                }
            }
        });


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
        startActivityForResult(intent, CameraActivity.CAMERA_RESULT);

    }

    // This is called after the "chooseFromLibrary" button is pressed.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Update Image Preview
            // START
            uri = data.getData();
            picturePath = uri;
            Log.d("URI: ", uri.toString());


            // END

        } else if (requestCode == CameraActivity.CAMERA_RESULT){
            uri = (Uri) data.getParcelableExtra(getString(R.string.camera_filename));
            double lat = (double) data.getSerializableExtra(getString(R.string.latitude));
            double lng = (double) data.getSerializableExtra(getString(R.string.longitude));
        }


        if (uri != null) {
            ImageView imageView = (ImageView) findViewById(R.id.uploadPreview);
            Picasso.with(this).invalidate(uri);
            Picasso.with(this)
                    .load(uri)
                    .placeholder(android.R.drawable.picture_frame)
                    .fit()
                    .centerInside()
                    .into(imageView);
        }
    }

}
