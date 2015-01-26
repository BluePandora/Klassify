package com.betelguese.shoppingapploginscreen.appdata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.betelguese.shoppingapploginscreen.activities.LogInActivity;
import com.betelguese.shoppingapploginscreen.activities.StartUpActivity;

/**
 * Created by tuman on 26/1/2015.
 */
public class AppManager {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private static final int PRIVATE_MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "ShopperCartPref";
    private static final String KEY_FIRST_TIMER = "timer";

    public AppManager(Activity activity) {
        this.activity = activity;
        mSharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mSharedPreferences.edit();
        editor.apply();
    }

    private SharedPreferences getSharedPreferences(final String prefName, final int mode) {
        return this.activity.getSharedPreferences(prefName, mode);
    }

    public void openingActivity() {
        final Intent intent;
        if (isFirstTime()) {
            editor.putBoolean(KEY_FIRST_TIMER, false);
            editor.commit();
            intent = new Intent(activity, StartUpActivity.class);
        } else {
            intent = new Intent(activity, LogInActivity.class);
        }
        activity.finish();
        activity.startActivity(intent);

    }

    private boolean isFirstTime() {
        return mSharedPreferences.getBoolean(KEY_FIRST_TIMER, true);
    }


}
