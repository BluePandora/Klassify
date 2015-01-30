package com.betelguese.shoppingapploginscreen.appdata;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.betelguese.shoppingapploginscreen.R;
import com.betelguese.shoppingapploginscreen.activities.Home;

/**
 * Created by Ashraful on 1/21/2015.
 * Md.Ashraful Islam
 * Reg No. 2010331035
 * Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */
public class MyActionBarDrawerToggle extends ActionBarDrawerToggle {
    private Home actionBarActivity;

    public MyActionBarDrawerToggle(Home activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        this.actionBarActivity = activity;
    }

    @Override
    public void onDrawerClosed(View view) {
        actionBarActivity.supportInvalidateOptionsMenu();
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        actionBarActivity.supportInvalidateOptionsMenu();
    }
}
