package com.example.apt_miniproject_android.backend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.support.design.widget.Snackbar;

import com.android.volley.VolleyError;

/**
 * Created by eric on 10/20/17.
 */

public class DefaultServerErrorAction implements ServerErrorAction {

    private View mView;

    public DefaultServerErrorAction(View view) {
        mView = view;
    }

    @Override
    public void handleError(VolleyError error) {
        Snackbar.make(mView, "Error retrieving data from server", Snackbar.LENGTH_LONG).show();
        Log.e("CommError", error.getMessage());
    }
}
