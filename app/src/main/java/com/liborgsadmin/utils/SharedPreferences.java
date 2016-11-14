package com.liborgsadmin.utils;

import android.content.Context;

/**
 * Created by sakshiagarwal on 02/11/16.
 */

public class SharedPreferences {

    private final String AUTH_KEY = "auth_key";
    private final String REG_ID = "reg_id";


    public void storeAuthKey(Context context, String auth_key) {
        final android.content.SharedPreferences prefs = context.getSharedPreferences(ApplicationConstants.APP_SHARED_PREFS, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AUTH_KEY, auth_key);
        editor.apply();
    }

    public String getAuthKey(Context context) {
        final android.content.SharedPreferences prefs = context.getSharedPreferences(ApplicationConstants.APP_SHARED_PREFS, Context.MODE_PRIVATE);
        String auth_key = prefs.getString(AUTH_KEY, "");
        if (auth_key.isEmpty()) {
            return "";
        }
        return auth_key;
    }

    public void deleteAuthKey(Context context)
    {
        android.content.SharedPreferences sharedpreferences = context.getSharedPreferences(ApplicationConstants.APP_SHARED_PREFS, Context.MODE_PRIVATE);
        sharedpreferences.edit().remove(AUTH_KEY).apply();
    }

    public void storeGCMRegId(Context context, String reg_id) {
        final android.content.SharedPreferences prefs = context.getSharedPreferences(ApplicationConstants.APP_SHARED_PREFS, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, reg_id);
        editor.apply();
    }

    public String getGCMRegId(Context context) {
        final android.content.SharedPreferences prefs = context.getSharedPreferences(ApplicationConstants.APP_SHARED_PREFS, context.MODE_PRIVATE);
        String regId = prefs.getString(REG_ID, "");
        if (regId.isEmpty()) {
            return "";
        }
        return regId;
    }
}
