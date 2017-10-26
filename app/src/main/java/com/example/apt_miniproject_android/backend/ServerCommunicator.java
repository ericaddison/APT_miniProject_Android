package com.example.apt_miniproject_android.backend;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apt_miniproject_android.R;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by eric on 10/20/17.
 */

public class ServerCommunicator {

    private RequestQueue mQueue;
    private Context mContext;
    private ServerErrorAction errorAction;

    public ServerCommunicator(View view){
        mContext = view.getContext();
        mQueue = Volley.newRequestQueue(mContext);
        errorAction = new DefaultServerErrorAction(view);
    }


    public ServerCommunicator setErrorAction(ServerErrorAction errorAction) {
        this.errorAction = errorAction;
        return this;
    }


    /**
     * Request the "ViewStream" data from the server
     */
    public void requestViewStreamData(String streamID, int id1, int id2, ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_view_stream))
                .appendQueryParameter(mContext.getString(R.string.url_parm_stream_id), streamID)
                .appendQueryParameter(mContext.getString(R.string.url_parm_image_range), id1+"-"+id2);
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    /**
     * Request the "Management" data from the server
     */
    public void requestManagementData(String userID, ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_management))
                .appendQueryParameter(mContext.getString(R.string.url_parm_user_id), userID);
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    /**
     * Request "SearchStreams" data from the server
     */
    public void requestSearchStreamsData(String term, ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_search_streams))
                .appendQueryParameter(mContext.getString(R.string.url_parm_search_term), term);
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    /**
     * Request "SearchTags" data from the server
     */
    public void requestSearchTagsData(String term, ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_search_tags))
                .appendQueryParameter(mContext.getString(R.string.url_parm_search_term), term);
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }

    /**
     * Request "SearchTags" data from the server
     */
    public void requestUploadURL(ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_get_upload_url));
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    /**
     * Request "BatchStreamInfo" data from the server
     */
    public void requestStreamInfoData(List<String> streamIDs, ServerResponseAction callbackAction){

        JSONArray arr = new JSONArray(streamIDs);

        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_streaminfo))
                .appendQueryParameter(mContext.getString(R.string.url_parm_search_term), arr.toString());
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    public void requestSubscribedStreamsInfoData(String authToken, ServerResponseAction callbackAction){
        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_subscribed_streams))
                .appendQueryParameter(mContext.getString(R.string.url_auth_token_term), authToken);
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }


    /**
     * Request "StreamInfo" for all streams from the server
     */
    public void requestAllStreamInfoData(ServerResponseAction callbackAction){

        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_streaminfo));
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }

    /**
     * Request "StreamItemInfo" for all streams from the server
     */
    public void requestAllStreamItemInfoData(ServerResponseAction callbackAction){

        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_streamiteminfo));
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }

    /**
     * Request "StreamItemInfo" for one stream from the server
     */
    public void requestStreamItemInfoData(long streamID, ServerResponseAction callbackAction){

        Uri.Builder uri = getBaseServicesUri();
        uri.appendPath(mContext.getString(R.string.url_service_streamiteminfo));
        uri.appendQueryParameter("streamid", Long.toString(streamID));
        String myUrl = uri.build().toString();
        doRequest(myUrl, callbackAction);
    }

    private Uri.Builder getBaseServicesUri(){
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme(mContext.getString(R.string.url_scheme))
                .encodedAuthority(mContext.getString(R.string.url_authority))
                .appendPath(mContext.getString(R.string.url_services));
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
                errorAction.handleError(error);
            }
        });

        // Add the request to the RequestQueue.
        mQueue.add(stringRequest);
    }




}
