package com.example.apt_miniproject_android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apt_miniproject_android.model.StreamInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 10/23/17.
 */

public class StreamGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<StreamInfo> mstreamInfos;
    private int width;
    private GridView.LayoutParams parms;

    public StreamGridViewAdapter(Context c) {
        mContext = c;
        mstreamInfos = new ArrayList<>();
    }

    private void setWidth(View parent) {
        int parent_width = parent.getMeasuredWidth();
        int spacing = parent_width / 25;
        width = (parent_width - 3 * spacing) / 4;

        parms = new GridView.LayoutParams(width, 100 + width);

        if( parent instanceof GridView) {
            ((GridView)parent).setHorizontalSpacing(spacing);
            ((GridView)parent).setVerticalSpacing(spacing);
        }
    }


    public void clear(){
        mstreamInfos.clear();
    }


    public int getCount() {
        return mstreamInfos.size();
    }


    public StreamInfo getItem(int position) {
        return mstreamInfos.get(position);
    }


    public long getItemId(int position) {
        return mstreamInfos.get(position).getId();
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        myImageView imageView;

        setWidth(parent);

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
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

            if(!streamInfo.getCoverImageURL().equals(""))
                Picasso.with(mContext)
                        .load(streamInfo.getCoverImageURL())
                        .placeholder(android.R.drawable.picture_frame)
                        .fit()
                        .centerCrop()
                        .into(imageView);
            else
                Picasso.with(mContext)
                        .load(android.R.drawable.ic_menu_close_clear_cancel)
                        .fit()
                        .centerCrop()
                        .into(imageView);

            textView.setText(streamInfo.getName());

            this.addView(imageView);
            this.addView(textView);
        }


    }

}
