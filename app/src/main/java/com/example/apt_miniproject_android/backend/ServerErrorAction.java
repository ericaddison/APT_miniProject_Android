package com.example.apt_miniproject_android.backend;

import com.android.volley.VolleyError;

/**
 * Created by eric on 10/20/17.
 */

public interface ServerErrorAction {

    public void handleError(VolleyError error);

}
