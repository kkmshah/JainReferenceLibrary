package com.jainelibrary.manager;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionManager {

    public static boolean checkInternetConnection(Context context) {
        if (context == null)
            return false;
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr != null && conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;
        } else {
            return false;
        }
    }

}