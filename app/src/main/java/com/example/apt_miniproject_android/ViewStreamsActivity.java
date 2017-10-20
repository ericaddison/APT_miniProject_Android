package com.example.apt_miniproject_android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apt_miniproject_android.backend.ServerCommunicator;
import com.example.apt_miniproject_android.backend.ServerResponseAction;
import com.example.apt_miniproject_android.model.StreamInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewStreamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);

        // set gridview adapter and click behavior
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        final ImageURLAdapter adapter = new ImageURLAdapter(this);
        gridview.setAdapter(adapter);

        // load stream data from server
        ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));
        comm.requestAllStreamInfoData(new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                Gson gson = new GsonBuilder().create();
                StreamInfo[] streams = gson.fromJson(response, StreamInfo[].class);
                for(StreamInfo stream : streams)
                    adapter.addThumbURL(new ImageURL(stream.getCoverImageURL(), stream.getName()));
                for(StreamInfo stream : streams)
                    adapter.addThumbURL(new ImageURL(stream.getCoverImageURL(), stream.getName()));
                for(StreamInfo stream : streams)
                    adapter.addThumbURL(new ImageURL(stream.getCoverImageURL(), stream.getName()));
                gridview.invalidate();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ViewStreamsActivity.this, "You clicked on image " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // set "nearby" click behavior
        ImageView nearbyView = (ImageView) findViewById(R.id.nearby_image);
        nearbyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewStreamsActivity.this, "Go to nearby streams page...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // set search button behavior
        Button searchButton = (Button) findViewById(R.id.button_viewstreams_search);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewStreamsActivity.this, "Search for streams...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // set subscribed button behavior
        Button subButton = (Button) findViewById(R.id.button_viewstreams_sub);
        subButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewStreamsActivity.this, "Go to subscribed stream page...",
                        Toast.LENGTH_SHORT).show();
            }
        });

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
        private List<ImageURL> mThumbURLs;

        public ImageURLAdapter(Context c) {
            mContext = c;
            mThumbURLs = new ArrayList<>();
        }

        public int getCount() {
            return mThumbURLs.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            myImageView imageView = new myImageView(mContext);
            int parent_width = parent.getMeasuredWidth();
            int spacing = parent_width/25;
            int width = (parent_width-3*spacing)/4;
            GridView.LayoutParams parms = new GridView.LayoutParams(width, 100+width);
            ((GridView)parent).setHorizontalSpacing(spacing);
            ((GridView)parent).setVerticalSpacing(spacing);
            imageView.setLayoutParams(parms);

            if(!mThumbURLs.get(position).name.equals(""))
                imageView.setImageURL(mThumbURLs.get(position), width);

            return imageView;
        }

        public void addThumbURL(ImageURL url){
            mThumbURLs.add(url);
        }

        private class myImageView extends LinearLayout {

            private ImageView imageView;
            private TextView textView;

            public myImageView(Context context) {
                super(context);
                this.setOrientation(LinearLayout.VERTICAL);
            }

            public void setImageURL(ImageURL url, int width){

                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(width, width));

                textView = new TextView(mContext);

                if(!url.url.equals(""))
                    Picasso.with(mContext).load(url.url).into(imageView);
                textView.setText(url.name);

                this.addView(imageView);
                this.addView(textView);
            }


        }

    }


}
