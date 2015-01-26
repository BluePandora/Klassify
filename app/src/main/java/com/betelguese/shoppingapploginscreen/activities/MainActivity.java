package com.betelguese.shoppingapploginscreen.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.betelguese.shoppingapploginscreen.appdata.AppManager;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager appManager = new AppManager(this);
        appManager.openingActivity();
    }
}
