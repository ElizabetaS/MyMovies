package com.elisp.mymovies;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

/**
 * Created by elisp on 03.3.2018.
 */

public class CheckConnection {
    public static Boolean CheckInternetConnectivity(final Context con, final boolean show, final Runnable callback) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeConnection = connectivityManager.getActiveNetworkInfo();

            if (activeConnection == null)
                throw new Exception();
            if (!activeConnection.isConnectedOrConnecting())
                throw new Exception();
            return true;
        } catch (Exception ex) {

            if (show) {
                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle(con.getString(R.string.check_internet_title)).setMessage(con.getString(R.string.check_internet_message))
                        .setPositiveButton(con.getString(R.string.check_internet_settings), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (CheckInternetConnectivity(con, show, callback))
                                    callback.run();
                            }
                        }).setNegativeButton(con.getString(R.string.check_internet_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ((Activity) con).finish();
                        } catch (Exception e) {

                        }
                    }
                }).setCancelable(false);
                try {
                    builder.create().show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
}
