

package com.cscodetech.pharmafast.locationpick;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.cscodetech.pharmafast.R;

public class MapUtility {
    public static final String MAP_URL = "https://maps.googleapis.com";

    public static String apiKey = "*****************************************";
    public static Location currentLocation = null;
    public static Dialog popupWindow;
    public static String address = "address";
    public static String latitude = "lat";
    public static String longitude = "long";


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showToast(Context context, String message) {
        try {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } catch (Exception ex) {
ex.printStackTrace();
        }
    }


    public static void showProgress(final Context context) {
        try {
            if (!((Activity) context).isFinishing()) {
                View layout = LayoutInflater.from(context).inflate(R.layout.popup_loading, null);
                popupWindow = new Dialog(context, android.R.style.Theme_Translucent);
                popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popupWindow.setContentView(layout);
                popupWindow.setCancelable(false);
                if (!((Activity) context).isFinishing()) {
                    popupWindow.show();
                }
            }

        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }


    public static void hideProgress() {
        try {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
