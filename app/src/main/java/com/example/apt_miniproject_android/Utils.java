package com.example.apt_miniproject_android;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by eric on 10/26/17.
 */

public class Utils {


    public static void uiThreadShortToast(final Activity act, final String message){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static long getUsedMemorySize() {

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;

    }

}
