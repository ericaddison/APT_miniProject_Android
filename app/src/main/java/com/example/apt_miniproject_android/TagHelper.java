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

public class TagHelper {
    Context mContextCustomUpload = null;

    public TagHelper(Context context) {
        mContextCustomUpload = context;
    }

    public TagHelper() {
    }

    public static void addTag(String tag, String streamID) {
        RequestParams params = new RequestParams();
        params.put("tagName", tag);
        params.put("streamID", streamID);
        params.put("submit", "Add Tag");
//            params.put("redirect", "https://apt17-miniproj-whiteteam.appspot.com/viewstream?streamID=" + streamID);

        AsyncHttpClient client = new AsyncHttpClient();


        //TODO remove hard coding of url
        String postURL = "https://apt17-miniproj-whiteteam.appspot.com/tagmod";
        //String postURL = "http://httpbin.org/post";

        client.post(postURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d(this.getClass().getSimpleName(), "upload success! ");
                String content = null;
                try {
                    content = new String(responseBody, "UTF-8"); // from http://stackoverflow.com/q/26787928
                    Log.d(this.getClass().getSimpleName(), "response:" + content);

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