package com.example.apt_miniproject_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ViewStreamsButtonFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_streams_button, container, false);

        Button viewStreamsButton = (Button) v.findViewById(R.id.button_view_streams);
        viewStreamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoStreamsPage(view);
            }
        });

        return v;

    }

    public void gotoStreamsPage(View v){
        Toast.makeText(v.getContext(), "Going to streams page...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(v.getContext(), ViewStreamsActivity.class);
        startActivity(i);
    }

}