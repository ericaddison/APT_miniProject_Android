package com.example.apt_miniproject_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    private GridView gridview;
    private StreamCoverImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // set gridview adapter and click behavior
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new StreamCoverImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ViewStreamsActivity.this, "Stream " + adapter.getItem(position).getId(),
                        Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(v.getContext(), ViewStreamActivity.class);
                //i.putExtra(adapter.getItem(position).getId());
                //startActivity(i);
            }
        });

        // set "nearby" click behavior
        View nearbyView = (View) findViewById(R.id.nearby_image);
        nearbyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewStreamsActivity.this, "Go to nearby streams page...",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), FindNearbyActivity.class);
                startActivity(i);
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

    @Override
    protected void onStart() {
        super.onStart();

        ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));
        comm.requestAllStreamInfoData(new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                Gson gson = new GsonBuilder().create();
                StreamInfo[] streams = gson.fromJson(response, StreamInfo[].class);
                for(StreamInfo stream : streams)
                    adapter.addStreamInfo(stream);
                adapter.notifyDataSetChanged();
            }
        });
    }


    public class StreamCoverImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<StreamInfo> mstreamInfos;
        private int width;
        private GridView.LayoutParams parms;

        public StreamCoverImageAdapter(Context c) {
            mContext = c;
            mstreamInfos = new ArrayList<>();
        }

        private void setWidth(int parent_width) {
            int spacing = parent_width / 25;
            width = (parent_width - 3 * spacing) / 4;

            parms = new GridView.LayoutParams(width, 100 + width);
            gridview.setHorizontalSpacing(spacing);
            gridview.setVerticalSpacing(spacing);
        }


        public int getCount() {
            return mstreamInfos.size();
        }

        public StreamInfo getItem(int position) {
            return mstreamInfos.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            myImageView imageView;

            if (width != parent.getMeasuredWidth())
                setWidth(parent.getMeasuredWidth());

            imageView = new myImageView(mContext);
            imageView.setLayoutParams(parms);

            if(!mstreamInfos.get(position).getName().equals(""))
                imageView.setStreamInfo(mstreamInfos.get(position), width);

            return imageView;
        }

        public void addStreamInfo(StreamInfo info){
            mstreamInfos.add(info);
        }

        private class myImageView extends LinearLayout {

            private ImageView imageView;
            private TextView textView;

            public myImageView(Context context) {
                super(context);
                this.setOrientation(LinearLayout.VERTICAL);
            }

            public void setStreamInfo(final StreamInfo streamInfo, int width){

                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(width, width));

                textView = new TextView(mContext);

                if(!streamInfo.getCoverImageURL().equals(""))
                    Picasso.with(mContext)
                            .load(streamInfo.getCoverImageURL())
                            .placeholder(android.R.drawable.picture_frame)
                            .into(imageView);

                textView.setText(streamInfo.getName());

                this.addView(imageView);
                this.addView(textView);
            }


        }

    }


}
