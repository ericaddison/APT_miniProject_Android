package com.example.apt_miniproject_android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.vision.text.Text;

/**
 * Created by eric on 10/25/17.
 */

public abstract class BaseActivity extends AppCompatActivity{

    private GoogleSigninHelper signinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signinHelper = new GoogleSigninHelper(this, this.getClass().getSimpleName());
    }


    @Override
    protected void onResume() {
        super.onResume();
        TextView logoutView = (TextView) findViewById(R.id.title_logout_textView);
        if(logoutView!=null){
            if(getSignInAccount()==null)
                logoutView.setText(getString(R.string.login_text));
            else
                logoutView.setText(getString(R.string.logout_text));
        }
    }

    public GoogleApiClient getApiClient(){
        return signinHelper.getGoogleApiClient();
    }

    public GoogleSignInAccount getSignInAccount(){
        return signinHelper.getSignInAccount();
    }

    public GoogleSigninHelper getSigninHelper(){
        return signinHelper;
    }

}
