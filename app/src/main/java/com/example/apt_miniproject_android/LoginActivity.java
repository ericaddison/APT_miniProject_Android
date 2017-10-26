package com.example.apt_miniproject_android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView mStatusTextView;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        mStatusTextView = (TextView) findViewById(R.id.login_text);

    }


    @Override
    protected void onResume() {
        super.onResume();

        boolean logout = getIntent().getBooleanExtra(getString(R.string.logout_extra_parm), false);
        if( logout ) {
            Log.d(TAG, "Logging out!");
            getSigninHelper().signOut(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    mStatusTextView.setText(getString(R.string.default_login_text));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "clicked login button");
        switch (v.getId()) {
            case R.id.sign_in_button:
                getSigninHelper().signIn(RC_SIGN_IN);
                break;
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            getSigninHelper().setSignInAccount(result.getSignInAccount());
            mStatusTextView.setText(" Logged in as " + getSignInAccount().getDisplayName());
        } else {
            // Signed out, show unauthenticated UI.
            Log.d(TAG, "handleSignInResult: BARF");
            mStatusTextView.setText(getString(R.string.default_login_text));
        }
    }

}
