package com.aarshinkov.mobile.hotelly.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.aarshinkov.mobile.hotelly.responses.users.UserGetResponse;
import com.google.gson.Gson;

import static com.aarshinkov.mobile.hotelly.utils.Constants.SHARED_PREF_USER;

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

        UserGetResponse user = getLoggedUser(pref);

        if (user == null) {
            return false;
        }

        return user.getUserId() != null;
    }

    public static UserGetResponse getLoggedUser(SharedPreferences pref) {

        if (pref == null) {
            return null;
        }
        Gson gson = new Gson();
        String prefUser = pref.getString(SHARED_PREF_USER, null);
        if (prefUser == null) {
            return null;
        }
        return gson.fromJson(prefUser, UserGetResponse.class);
    }
}
