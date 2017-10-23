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

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = UploadActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private String picturePath = "NULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Set the upload edit text hint
        EditText uploadHint;
        uploadHint = (EditText) findViewById(R.id.uploadEditText);
        uploadHint.setHint("Add a message and/or tags");
        uploadHint.setGravity(Gravity.TOP);

        // Set stream name
        EditText streamName = (EditText) findViewById(R.id.streamName);
        streamName.setEnabled(false);

        //TODO set stream name edit text
        streamName.setText("OH MY!!!");


    }


    // BUTTON LISTENERS

    //TODO upload image button
    public void uploadImage(View view) {
        EditText uploadEditText = (EditText) findViewById(R.id.uploadEditText);
        String content = uploadEditText.getText().toString();
        Log.v("UploadText", content);
        Log.d("Picture path: ", this.picturePath);


//        String bucketName = "apt17-miniproj-whiteteam.appspot.com";


//        //get message from message box
//        String  msg = msgTextField.getText().toString();
//
//        //check whether the msg empty or not
//        if(msg.length()>0) {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost("http://www.yourdomain.com/serverside-script.php");
//
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("id", "01"));
//                nameValuePairs.add(new BasicNameValuePair("message", msg));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                httpclient.execute(httppost);
//                msgTextField.setText(""); //reset the message text field
//                Toast.makeText(getBaseContext(),"Sent",Toast.LENGTH_SHORT).show();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            //display message if text field is empty
//            Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_SHORT).show();
//        }


    }

    //TODO choose from library button
    public void chooseFromLibrary(View view) {
        Log.d(TAG, "Choosing image from library!");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //TODO use camera button
    public void useCamera(View view) {
        Log.d(TAG, "CameraButton Pressed!");

        // Go to Camera Activity
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);

    }


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
