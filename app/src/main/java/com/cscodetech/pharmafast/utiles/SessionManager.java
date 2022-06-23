package com.cscodetech.pharmafast.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cscodetech.pharmafast.model.AddressList;
import com.cscodetech.pharmafast.model.User;
import com.google.gson.Gson;

public class SessionManager {

    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static String login = "login";
    public static String isopen = "isopen";
    public static String user = "users";
    public static String dcharge = "dcharge";
    public static String address = "address";


    public static String currency = "currency";
    public static String lats = "lats";
    public static String longs = "longs";
    public static String SELCTADDRESS = "selctaddress";
    public static String coupon = "coupon";
    public static String couponid = "couponid";


    public static String terms = "terms";
    public static String contact = "contact";
    public static String about = "about";
    public static String policy = "policy";
    public static String uploadpref = "uploadpref";
    public static String mobile = "mobile";

    public SessionManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }

    public void setFloatData(String key, float val) {
        mEditor.putFloat(key, val);
        mEditor.commit();
    }

    public float getFloatData(String key) {
        return mPrefs.getFloat(key, 0);
    }

    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void setUserDetails(String key, User val) {
        mEditor.putString(user, new Gson().toJson(val));
        mEditor.commit();
    }

    public User getUserDetails(String key) {
        return new Gson().fromJson(mPrefs.getString(user, ""), User.class);
    }

    public void setAddress(AddressList val) {
        mEditor.putString("saddress", new Gson().toJson(val));
        mEditor.commit();
    }

    public AddressList getAddress() {
        return new Gson().fromJson(mPrefs.getString("saddress", ""), AddressList.class);
    }


    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
