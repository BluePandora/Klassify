package com.betelguese.klassify.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.Category;
import com.betelguese.klassify.appdata.CategoryAdapter;
import com.betelguese.klassify.appdata.MyActionBarDrawerToggle;
import com.betelguese.klassify.appdata.NavAdapter;
import com.betelguese.klassify.fragments.BaseFragment;
import com.betelguese.klassify.fragments.CategoryBaseFragment;
import com.betelguese.klassify.fragments.CategoryFragment;
import com.betelguese.klassify.utils.Config;
import com.betelguese.klassify.utils.OnMessageListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategoryHome extends ActionBarActivity implements OnMessageListener {
    public ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private TextView fullName, email;
    private ImageView profilePicture;
    public static ArrayList<Category> categories;
    private CategoryAdapter adapter;

    public static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //View header = getLayoutInflater().inflate(R.layout.account, null);
        //fullName = (TextView) header.findViewById(R.id.title);
        //email = (TextView) header.findViewById(R.id.email);
        //profilePicture = (ImageView) header.findViewById(R.id.image);
        //fullName.setText("Ashraful " + "Islam");
        //email.setText("ashrafulcse.sust@gmail.com");
        //mDrawerList.addHeaderView(header, mTitle, false);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.load);
        adapter = new CategoryAdapter(this, progressBar, null, 0, null, null);
        mDrawerList.setAdapter(adapter);
        adapter.setData(categories);
        adapter.invalidate();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new MyActionBarDrawerToggle(this, mDrawerLayout);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(getIntent().getIntExtra(Config.ARG_POSITION, 0));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.btn_action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //If the nav drawer is open, hide action items related to the content
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
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
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setArgument(Fragment fragment, int argument) {
        Bundle args = new Bundle();
        args.putParcelable(Config.ARG_CATEGORY_ITEM, adapter.getData(argument));
        fragment.setArguments(args);
    }


    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = chooseFragment(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, adapter.getTitle(position)).commit();
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        getSupportActionBar().setTitle(adapter.getTitle(position));
    }

    private Fragment chooseFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            default:
                fragment = new CategoryBaseFragment();
                setArgument(fragment, position);
        }
        return fragment;
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


    @Override
    public void onReceiveMessage(Bundle bundle) {

    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}