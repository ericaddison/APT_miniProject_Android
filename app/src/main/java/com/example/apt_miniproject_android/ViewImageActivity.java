package com.example.apt_miniproject_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.apt_miniproject_android.model.StreamItemInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewImageActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        mContext = getApplicationContext();

        String imageURL = getIntent().getExtras().getString("imageURL");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if(!imageURL.equals(""))
            Picasso.with(mContext)
                    .load(imageURL)
                    .placeholder(android.R.drawable.picture_frame)
                    .into(imageView);
        else
            Picasso.with(mContext)
                    .load(android.R.drawable.ic_menu_close_clear_cancel)
                    .into(imageView);
    }
}
