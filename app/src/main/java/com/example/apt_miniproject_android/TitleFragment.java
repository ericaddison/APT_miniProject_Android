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


public class TitleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_title, container, false);

        TextView signoutView = (TextView) v.findViewById(R.id.title_logout_textView);
        signoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tview = (TextView)view;
                if(tview.getText().equals(getString(R.string.logout_text)))
                    logout(view);
                else
                    login(view);
            }
        });

        return v;

    }

    public void logout(View v){
        Toast.makeText(v.getContext(), "Signing out of Google account", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(v.getContext(), LoginActivity.class);
        i.putExtra( getString(R.string.logout_extra_parm), true);
        startActivity(i);
    }

    public void login(View v){
        Intent i = new Intent(v.getContext(), LoginActivity.class);
        startActivity(i);
    }

}