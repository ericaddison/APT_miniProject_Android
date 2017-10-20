package com.example.apt_miniproject_android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by eric on 10/20/17.
 */

public class ServerCommunicator {

    private RequestQueue mQueue;
    private Context mContext;

    public ServerCommunicator(Context context){
        mContext = context;
        mQueue = Volley.newRequestQueue(context);

    }

    //5631383682678784;imageRange=1-2
    public void requestViewStreamData(String streamID, int id1, int id2, ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath("viewstream")
                .appendQueryParameter("streamID", streamID)
                .appendQueryParameter("imageRange", id1+"-"+id2);
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    private Uri.Builder getBaseServicesUri(){
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("http")
                .authority("apt17-miniproj-whiteteam.appspot.com")
                .appendPath("services");
    }


    /**
     * Make the actual HTTP request
     */
    private void doRequest(String url, final ServerResponseAction callbackAction){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callbackAction.handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), "NO GOOD!!!!");
            }
        });

        // Add the request to the RequestQueue.
        mQueue.add(stringRequest);
    }




}
