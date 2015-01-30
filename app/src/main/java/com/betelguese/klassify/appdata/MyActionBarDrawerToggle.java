package com.betelguese.klassify.appdata;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.betelguese.klassify.R;
import com.betelguese.klassify.activities.Home;

/**
 * Created by Ashraful on 1/21/2015.
 * Md.Ashraful Islam
 * Reg No. 2010331035
 * Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */
public class MyActionBarDrawerToggle extends ActionBarDrawerToggle {
    private ActionBarActivity actionBarActivity;

    public MyActionBarDrawerToggle(ActionBarActivity activity, DrawerLayout drawerLayout) {
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
