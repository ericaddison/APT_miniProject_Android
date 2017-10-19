package com.example.apt_miniproject_android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ViewStreamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);



        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ViewStreamsActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbURLs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Picasso.with(mContext).load(mThumbURLs[position]).into(imageView);
            return imageView;
        }

        // references to our images
        private String[] mThumbURLs = {
                "https://cdn.pixabay.com/photo/2017/10/04/21/36/fly-agaric-2817723__340.jpg",
                "https://cdn.pixabay.com/photo/2017/09/08/20/29/chess-2730034__340.jpg",
                "https://cdn.pixabay.com/photo/2017/05/25/15/08/jogging-2343558__340.jpg",
                "https://cdn.pixabay.com/photo/2017/09/30/15/10/pizza-2802332__340.jpg",
                "https://cdn.pixabay.com/photo/2017/09/27/12/55/tiger-2791980__340.jpg",
                "https://cdn.pixabay.com/photo/2017/09/24/19/20/moorabbis-2782862__340.jpg",
                "https://cdn.pixabay.com/photo/2017/09/01/20/23/ford-2705402__340.jpg",
                "https://cdn.pixabay.com/photo/2017/09/23/11/43/football-2778583__340.jpg",
                "https://cdn.pixabay.com/photo/2017/08/19/10/00/eagle-2657888__340.jpg"
        };
    }


}
