package hu.rka.talkfollow;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Réka on 2016.02.02..
 */
public class PreferencesHelper {
    public static final String AUTHTOKEN = "Auth-Token";

    public static String getStringByKey(Context context, String key, String def) {
        SharedPreferences preferences = context.getSharedPreferences(AUTHTOKEN, Context.MODE_PRIVATE);

        return preferences.getString(key, "");
    }

    public static void setStringByKey(Context context, String key, String value) {
        if ( context != null ) {
            SharedPreferences.Editor prefsEditor = getPrefsEditor(context);
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        }
    }

    private static SharedPreferences.Editor getPrefsEditor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AUTHTOKEN, Context.MODE_PRIVATE);
        return preferences.edit();

    }


}
