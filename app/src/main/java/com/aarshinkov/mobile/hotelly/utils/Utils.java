package com.aarshinkov.mobile.hotelly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_USER_ID;

public class Utils {

    public static String getStringResource(Context context, String resource) {

        try {
            int resId = context.getResources().getIdentifier(resource, "string", context.getPackageName());

            return context.getString(resId);

        } catch (Exception ignored) {
//            Log.e("ERROR", "Some error: ", e);
        }
        return "";
    }

    public static Boolean isLoggedIn(SharedPreferences pref) {

        String userId = pref.getString(SHARED_PREF_USER_ID, null);

        return userId != null;
    }
}
