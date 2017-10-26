package com.example.apt_miniproject_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by eric on 10/25/17.
 */

public class GoogleSigninHelper {

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount acct;
    AppCompatActivity parent;

    public GoogleSigninHelper(AppCompatActivity parent, final String TAG) {

        this.parent = parent;

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(parent.getString(R.string.oauth_client_id))
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(parent)
                .enableAutoManage(parent, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "doh! connection failed!");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (pendingResult.isDone()) {
            // There's immediate result available.
            acct = pendingResult.get().getSignInAccount();
        } else {
            // There's no immediate result ready, displays some progress indicator and waits for the
            // async callback.
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    acct = result.getSignInAccount();
                }
            });
        }

    }

    GoogleSignInAccount getSignInAccount(){
        return acct;
    }

    public void setSignInAccount(GoogleSignInAccount acct) {
        this.acct = acct;
    }

    GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }



    public void signIn(int RC_SIGN_IN) {
        mGoogleApiClient.connect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        parent.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void signOut(final ResultCallback<Status> signoutCallback) {
        if(mGoogleApiClient.isConnected())
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(signoutCallback);
        else if (mGoogleApiClient.isConnecting())
            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(signoutCallback);
                }

                @Override
                public void onConnectionSuspended(int i) {}
            });
        acct = null;
    }



}
