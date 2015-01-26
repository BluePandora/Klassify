package com.betelguese.shoppingapploginscreen.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.betelguese.shoppingapploginscreen.R;
import com.betelguese.shoppingapploginscreen.fragments.LogInFragment;
import com.betelguese.shoppingapploginscreen.fragments.StartUpSlidePageFragment;


public class StartUpActivity extends ActionBarActivity {

    private static final int NUM_PAGES = 4;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        mPager = (ViewPager) findViewById(R.id.start_up_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                    return StartUpSlidePageFragment.newInstance(position);
                case 3:
                    return new LogInFragment();
                default:
                    return new LogInFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
