package com.example.saojeong.login;

import android.app.Activity;
import android.content.SharedPreferences;
import com.example.saojeong.auth.TokenCase;
import java.util.Date;

public class LoginToken {
    private static String AccessToken = "";
    private static String RefreshToken = "";

    public static void setToken(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("SHARE_PREF", activity.MODE_PRIVATE);
        AccessToken = pref.getString("AccessToken", "");
        RefreshToken = pref.getString("RefreshToken", "");
    }

    public static int AccessTokenTimer() {
        if (AccessToken != null) {
            Date d = TokenCase.getExp();
            long now = System.currentTimeMillis();
            Date dateNow = new Date(now);
            long timer = d.getTime() - dateNow.getTime();
            long time2 = timer / (60 * 1000);
            time2 = Math.abs(time2);
            if (time2 < 0) {
                return 1;
            } else if (time2 < 15) {
                return 0;
            }
        }
        return 2;
    }

    public static void deleteToken(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("SHARE_PREF", activity.MODE_PRIVATE);
        AccessToken = null;
        RefreshToken = null;
        SharedPreferences.Editor editer = pref.edit();
        editer.remove("AccessToken");
        editer.remove("RefreshToken");
        editer.apply();
        editer.commit();
    }

    public static String getToken() {
        return AccessToken;
    }

    public static String getRefreshToken() {
        return RefreshToken;
    }

}
