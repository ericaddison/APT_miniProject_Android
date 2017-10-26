package com.example.apt_miniproject_android;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.apt_miniproject_android.backend.DefaultServerErrorAction;
import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerErrorAction;
import com.example.apt_miniproject_android.backend.ServerResponseAction;
import com.example.apt_miniproject_android.model.StreamItemInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FindNearbyActivity extends AbstractLocationActivity {

    private GridView gridview;
    private FindNearbyActivity.ImageURLAdapter adapter;


    public void viewAllStreamsOnClick(View view){
        Intent i = new Intent(view.getContext(), ViewStreamsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_nearby);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // set gridview adapter and click behavior
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new FindNearbyActivity.ImageURLAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Log.d("click position: ", Integer.toString(position));

                Object selectedItem = parent.getItemAtPosition(position);
                FindNearbyActivity.ImageURL itemURL = (FindNearbyActivity.ImageURL)selectedItem;
                Intent i = new Intent(v.getContext(), ViewImageActivity.class);
                i.putExtra("imageURL", itemURL.url);
                startActivity(i);
            }
        });
    }

    @Override
    protected void handleNewLocation(Location location) {
        Log.d(TAG, "handleNewLocation");

        View view = findViewById(android.R.id.content);

        final Location currentLocation = new Location("");
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());

        //Updates the TextView display with the current location
        TextView text = (TextView) findViewById(R.id.locTextView);
        text.setText("CurrLoc: " + location.getLatitude() + ", " + location.getLongitude());


        ServerCommunicator comm = new ServerCommunicator(view);

        ServerErrorAction errorAction = new DefaultServerErrorAction(view){
            @Override
            public void handleError(VolleyError error) {
                super.handleError(error);
                Log.e(TAG, "ServerCommunicator error");
            }
        };


        ServerResponseAction allStreamItemsCallback = new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                Gson gson = new GsonBuilder().create();
                StreamItemInfo[] items = gson.fromJson(response, StreamItemInfo[].class);

                ArrayList<StreamItemInfo> itemsWithLocation = new ArrayList<>();
                for(StreamItemInfo item : items) {
                    if (item.getLat() != "0" && item.getLng() != "0"){
                        itemsWithLocation.add(item);
                    }
                }

                for(StreamItemInfo item : itemsWithLocation){
                    try {
                        Log.d("Find distance item: ", item.getId() + " lat:" + item.getLat() + " lng:" + item.getLng());
                        Location itemLoc = new Location("");
                        itemLoc.setLatitude((long) Double.parseDouble(item.getLat()));
                        itemLoc.setLongitude((long) Double.parseDouble(item.getLng()));
                        float distanceInMeters = currentLocation.distanceTo(itemLoc);
                        String distance = "";
                        if (distanceInMeters < 10000) {
                            distance = Double.toString(Math.round(distanceInMeters * 1000.0) / 1000.0) + " M";
                        } else {
                            double distanceInKilometers = distanceInMeters / 1000.0000;
                            distance = Double.toString(Math.round(distanceInKilometers * 1000.0) / 1000.0) + " KM";
                        }

                        adapter.addThumbURL(new FindNearbyActivity.ImageURL(item.getImageUrl(), distance));
                    } catch (NumberFormatException e){
                        Log.d(TAG, e.getMessage());
                        continue;
                    }
                }

                adapter.notifyDataSetChanged();
            }
        };

        comm.setErrorAction(errorAction).requestAllStreamItemInfoData(allStreamItemsCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.clear();
    }



    public class ImageURL{
        public String url;
        public String name;

        public ImageURL(String url, String name) {
            this.url = url;
            this.name = name;
        }
    }

    public class ImageURLAdapter extends BaseAdapter {
        private Context mContext;
        private List<FindNearbyActivity.ImageURL> mThumbURLs;
        private int width;
        private GridView.LayoutParams parms;

        public ImageURLAdapter(Context c) {
            mContext = c;
            mThumbURLs = new ArrayList<>();
        }

        private void setWidth(int parent_width) {
            int spacing = parent_width / 25;
            width = (parent_width - 3 * spacing) / 4;

            parms = new GridView.LayoutParams(width, 100 + width);
            gridview.setHorizontalSpacing(spacing);
            gridview.setVerticalSpacing(spacing);
        }


        public void clear(){
            mThumbURLs.clear();
        }

        public int getCount() {
            return mThumbURLs.size();
        }

        public Object getItem(int position) {
            return mThumbURLs.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            FindNearbyActivity.ImageURLAdapter.myImageView imageView;

            if (width != parent.getMeasuredWidth())
                setWidth(parent.getMeasuredWidth());

            imageView = new FindNearbyActivity.ImageURLAdapter.myImageView(mContext);
            imageView.setLayoutParams(parms);

            if(!mThumbURLs.get(position).name.equals(""))
                imageView.setImageURL(mThumbURLs.get(position), width);

            return imageView;
        }

        public void addThumbURL(FindNearbyActivity.ImageURL url){
            mThumbURLs.add(url);
        }

        private class myImageView extends LinearLayout {

            private ImageView imageView;
            private TextView textView;

            public myImageView(Context context) {
                super(context);
                this.setOrientation(LinearLayout.VERTICAL);
            }

            public void setImageURL(final FindNearbyActivity.ImageURL url, int width){

                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(width, width));

                textView = new TextView(mContext);

                if(!url.url.equals(""))
                    Picasso.with(mContext)
                            .load(url.url)
                            .placeholder(android.R.drawable.picture_frame)
                            .into(imageView);

                textView.setText(url.name);

                this.addView(imageView);
                this.addView(textView);
            }


        }

    }

}
