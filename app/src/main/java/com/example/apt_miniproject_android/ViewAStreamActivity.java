package com.example.apt_miniproject_android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.apt_miniproject_android.model.StreamItemInfo;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewAStreamActivity extends BaseActivity {

    private GridView gridview;
    private ImageURLAdapter adapter;
    String streamName = "Unknown";
    String streamId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a_stream);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        streamId = Long.toString(getIntent().getExtras().getLong("streamID"));

        // set gridview adapter and click behavior
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new ImageURLAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Object selectedItem = parent.getItemAtPosition(position);
                ImageURL itemURL = (ImageURL)selectedItem;
                Intent i = new Intent(v.getContext(), ViewImageActivity.class);
                i.putExtra("imageURL", itemURL.url);
                startActivity(i);
            }
        });

        // set more images button behavior
        Button moreButton = (Button) findViewById(R.id.button_more_pictures);
        moreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ViewAStreamActivity.this, "Next 16 Images",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // set upload button behavior
        Button uploadButton = (Button) findViewById(R.id.button_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UploadActivity.class);
                i.putExtra("streamName", streamName);
                i.putExtra("streamID", streamId);
                startActivity(i);
            }
        });

        // set View All Streams button click behavior
        Button viewAllButton = (Button) findViewById(R.id.button_view_all_streams);
        viewAllButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ViewStreamsActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.clear();

        long longStreamID = getIntent().getExtras().getLong("streamID");
        Log.d("StreamID", Long.toString(longStreamID));
        //ServerComm request: '/services/streamiteminfo?streamid=5629499534213120'
        ServerCommunicator comm = new ServerCommunicator(findViewById(android.R.id.content));

        comm.requestStreamItemInfoData(longStreamID, new ServerResponseAction() {
            @Override
            public void handleResponse(String response) {
                Gson gson = new GsonBuilder().create();
                StreamItemInfo[] streamItems = gson.fromJson(response, StreamItemInfo[].class);
                Log.d("StreamItems: ", response);
                String streamName = "Stream Name: Unknown";
                for(StreamItemInfo item : streamItems) {
                    //StreamItems don't have names!!!  Just set name to "."
                    adapter.addThumbURL(new ImageURL(item.getImageUrl(), "."));
                    streamName = item.getStreamName();
                }

                TextView streamNameTextView = (TextView) findViewById(R.id.text_current_stream);
                streamNameTextView.setText("Stream Name: " + streamName);
                adapter.notifyDataSetChanged();
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
            myImageView imageView;

            if (width != parent.getMeasuredWidth())
                setWidth(parent.getMeasuredWidth());

            imageView = new myImageView(mContext);
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

            public void setImageURL(final ImageURL url, int width){

                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(width, width));


                if(!url.url.equals(""))
                    Picasso.with(mContext)
                            .load(url.url)
                            .placeholder(android.R.drawable.picture_frame)
                            .into(imageView);

                this.addView(imageView);
            }


        }

    }


}
