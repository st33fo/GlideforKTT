package com.st33fo.glideforktt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.securepreferences.SecurePreferences;

/**
 * Created by Stefan on 7/12/2016.
 */
public class SecuredSharePreference {

    static final String PREF_COOKIES ="cookies";
    static final String isUserLoggedIn ="USER_LOGGED_IN";



    static SharedPreferences getSecuredPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setCookies(Context ctx, String cookies) {
        SharedPreferences.Editor editor = getSecuredPreferences(ctx).edit();
        editor.putBoolean(SecuredSharePreference.isUserLoggedIn,true);
        editor.putString(PREF_COOKIES, cookies);
        editor.apply();
    }

    public static String getPrefCookies(Context ctx) {

            return getSecuredPreferences(ctx).getString(PREF_COOKIES, "");
        }




}

