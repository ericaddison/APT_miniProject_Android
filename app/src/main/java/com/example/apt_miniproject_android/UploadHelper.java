package com.example.apt_miniproject_android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.content.Context;
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
    public static void postImage(String uploadUrl, String filePath, String streamID, double lat, double lng){
        // http://loopj.com/android-async-http/
        // search for "Uploading Files with RequestParams"
        File imgFile = new File(filePath);
        RequestParams params = new RequestParams();
        try {
            params.put("file", imgFile);
            params.put("streamID", streamID);
            params.put("lat", lat);
            params.put("lng", lng);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("CustomUpload", "upload attempt " + params.toString());
        Log.d("CustomUpload", "file name: " + imgFile.getPath());
        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                String errorString = "";
                Log.d(this.getClass().getSimpleName(), "upload failure " + statusCode);
                error.printStackTrace(System.out);

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
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
        });
    }
}

/*
new AsyncHttpResponseHandler() {
	        @Override
	        public void onSuccess(String response) {
	            Log.w("async", "success!!!!");
	        }
	    }
*/