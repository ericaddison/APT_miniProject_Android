package com.example.apt_miniproject_android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    public static void postImage(String uploadUrl, InputStream input, String streamID, double lat, double lng, String authToken) {

        // http://loopj.com/android-async-http/
        RequestParams params = new RequestParams();
        params.setForceMultipartEntityContentType(true);
        params.put("file", input, "image", "image/jpeg");
        params.put("streamID", streamID);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("url", "");
        params.put("authToken", authToken);
        params.put("submit", "Submit");

        // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("postImage", uploadUrl);
        Log.d("postImage", "ID, lat, lng " + streamID + ", " + Double.toString(lat) + ", " + Double.toString(lng));

        // custom headers to match webapp
        client.addHeader("Accept-Encoding:", "gzip, deflate");
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        client.post(uploadUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d(this.getClass().getSimpleName(), "upload success! ");
                String content = null;
                try {
                    content = new String(responseBody, "UTF-8"); // from http://stackoverflow.com/q/26787928
                    //Log.d(this.getClass().getSimpleName(), "response:" + content);

                    logLargeString(content);

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
                String content = null;
                try {
                    content = new String(responseBody, "UTF-8"); // from http://stackoverflow.com/q/26787928
                    //Log.d(this.getClass().getSimpleName(), "response:" + content);

                    logLargeString(content);

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                error.printStackTrace(System.out);

            }
        });
    }

    // Help logging long responses
    public static void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("Response", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("Response", str); // continuation
        }
    }

}