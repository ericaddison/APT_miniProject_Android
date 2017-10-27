package com.example.apt_miniproject_android;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
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
import java.util.Random;

public class UploadActivity extends BaseActivity {
    private static final String TAG = UploadActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private String streamID = "0";
    private String streamName = "NULL";
    private Uri picturePath;
    private double lat = 0;
    private double lng = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

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

            // get name of stream from Server
            ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));

            comm.requestStreamItemInfoData(Long.parseLong(streamID), new ServerResponseAction() {
                @Override
                public void handleResponse(String response) {
                    Gson gson = new GsonBuilder().create();
                    StreamItemInfo[] streamItems = gson.fromJson(response, StreamItemInfo[].class);
                    for (StreamItemInfo item : streamItems)
                        streamName = item.getStreamName();

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


    // BUTTON LISTENERS

    public void uploadImage(View view) {
        EditText uploadEditText = (EditText) findViewById(R.id.uploadEditText);
        String content = uploadEditText.getText().toString();
        Log.v("UploadText", content);

        // get Upload URL
        ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));

        comm.requestUploadURL(new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                // Get unique upload URL
                String uploadURL = response;

                // MultiPart Form Post
                try {
//                    // Turn picture path into byte array
//                    byte[] data = null;
//                    try {
//                        ContentResolver cr = getBaseContext().getContentResolver();
//                        InputStream inputStream = cr.openInputStream(picturePath);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        data = baos.toByteArray();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    String encoded = Base64.encodeToString(data, Base64.DEFAULT);
//                     //print out byte array
//                    Log.v("Encoded", encoded);

                    //TODO CREATE POST
                    // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html

                    // shows content of post request
                    //UploadHelper.postImage("http://httpbin.org/post", picturePath, streamID, lat, lng);

                    // real request
                    UploadHelper.postImage(uploadURL, picturePath, streamID, lat, lng);


                } catch (Exception e) {
                    Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
                }
            }
        });


    }

    //Selects a image from a library
    public void chooseFromLibrary(View view) {
        Log.d(TAG, "Choosing image from library!");

        // set lat and lng to random values. Don't want to worry about edge cases.
        Random r = new Random();
        lat = -80 + r.nextDouble() * 160;  //between -80 and 80
        lng = -170.0 + r.nextDouble() * 340.0;  //between -170 and 170

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


            // END

        } else if (requestCode == CameraActivity.CAMERA_RESULT){
            uri = (Uri) data.getParcelableExtra(getString(R.string.camera_filename));
            picturePath = uri;
            lat = (double) data.getSerializableExtra(getString(R.string.latitude));
            lng = (double) data.getSerializableExtra(getString(R.string.longitude));
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
