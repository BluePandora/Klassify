package com.betelguese.shoppingapploginscreen.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.betelguese.shoppingapploginscreen.R;
import com.betelguese.shoppingapploginscreen.appdata.MyActionBarDrawerToggle;
import com.betelguese.shoppingapploginscreen.appdata.NavAdapter;
import com.betelguese.shoppingapploginscreen.fragments.BaseFragment;
import com.betelguese.shoppingapploginscreen.fragments.LogInFragment;
import com.betelguese.shoppingapploginscreen.fragments.SignUpFragment;
import com.betelguese.shoppingapploginscreen.fragments.StartUpSlidePageFragment;
import com.betelguese.shoppingapploginscreen.utils.Config;

import java.util.List;


/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class Home extends ActionBarActivity {
    public ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTitles;

    public static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        mTitle = mDrawerTitle = getTitle();
        mTitles = getResources().getStringArray(R.array.nav_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View header = getLayoutInflater().inflate(R.layout.account, null);
        ((TextView) header.findViewById(R.id.title)).setText("Ashraful " + "Islam");
        mDrawerList.addHeaderView(header, mTitle, false);
        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        // TypedArray typedImages =
        // getResources().obtainTypedArray(R.array.nav_images);
        // int len = typedImages.length();
        // int[] images = new int[len];
        // for (int i = 0; i < len; i++)
        // images[i] = typedImages.getResourceId(i, 0);
        // typedImages.recycle();
        mDrawerList.setAdapter(new NavAdapter(mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.item_share_fb).setVisible(!drawerOpen);
        //menu.findItem(R.id.item_share_tw).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
//            case R.id.item_share_fb:
//                Intent intent = new Intent(BDEmarket.this, ShareActivity.class);
//                intent.putExtra("type", "fb");
//                startActivity(intent);
//                return true;
//            case R.id.item_share_tw:
//                intent = new Intent(BDEmarket.this, ShareActivity.class);
//                intent.putExtra("type", "tw");
//                startActivity(intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setArgument(Fragment fragment, int argument) {
        Bundle args = new Bundle();
        args.putInt(Config.ARG_POSITION, argument);
        fragment.setArguments(args);
    }


    private void selectItem(int position) {
        if (position != 0) {
            // update the main content by replacing fragments

            Fragment fragment = chooseFragment(position);
            if (position == 1) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, mTitles[position - 1]).commit();

                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
                setTitle(mTitles[position - 1]);
            }
        }

    }

    private Fragment chooseFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new LogInFragment();
                setArgument(fragment, position);
                break;
            case 2:
                fragment = new SignUpFragment();
                setArgument(fragment, position);
                break;
            case 3:
                fragment = new StartUpSlidePageFragment();
                setArgument(fragment, position);
                break;
            case 4:
                fragment = new LogInFragment();
                setArgument(fragment, position);
                break;
            default:
                fragment = new BaseFragment();
                setArgument(fragment, position);
        }
        return fragment;
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    private Fragment getFragmentByPosition(int position) {
        String tag = mTitles[position];
        List<Fragment> list = getSupportFragmentManager().getFragments();
        for (Fragment fragment : list) {
            if (fragment != null && tag.equals(fragment.getTag()))
                return fragment;
        }
        return null;
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}