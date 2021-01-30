package com.example.mvvm_dagger_room.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class Connection {

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager
                = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
