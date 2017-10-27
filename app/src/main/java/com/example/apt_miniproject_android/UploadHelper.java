package com.example.apt_miniproject_android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UploadHelper {
    Context mContextCustomUpload = null;

    public UploadHelper(Context context) {
        mContextCustomUpload = context;
    }

    public UploadHelper() {
    }

    public static void postImage(String uploadUrl, Uri picturePath, String streamID, double lat, double lng) {
        // http://loopj.com/android-async-http/
        // search for "Uploading Files with RequestParams"
        File imgFile = new File(picturePath.getPath());
        RequestParams params = new RequestParams();
        try {
            params.put("file", imgFile);
            params.put("streamID", streamID);
            params.put("lat", lat);
            params.put("lng", lng);
            params.put("redirect", "https://apt17-miniproj-whiteteam.appspot.com/");    //not sure if needed.
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("postImage", "file name: " + imgFile.getPath());
        Log.d("postImage", uploadUrl);
        Log.d("postImage", "ID, lat, lng " + streamID + ", " + Double.toString(lat) + ", " + Double.toString(lng));
        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d(this.getClass().getSimpleName(), "upload success! ");
                String content = null;
                try {
                    content = new String(responseBody, "UTF-8"); // from http://stackoverflow.com/q/26787928
                    Log.d(this.getClass().getSimpleName(), "response:" + content);
//					sendDataToUrl(content);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                String errorString = "";
                Log.d(this.getClass().getSimpleName(), "upload failure " + statusCode);
                error.printStackTrace(System.out);

            }
        });
    }
}