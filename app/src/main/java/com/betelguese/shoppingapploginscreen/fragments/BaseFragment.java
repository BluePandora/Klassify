package com.betelguese.shoppingapploginscreen.fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.betelguese.shoppingapploginscreen.R;
import com.betelguese.shoppingapploginscreen.utils.Config;

import java.util.ArrayList;


/**
 * Created by Ashraful on 11/26/2014. Md.Ashraful Islam Reg No. 2010331035
 * Computer Science and Engineering Shahjalal University of Science and
 * Technology,Sylhet
 */

public class BaseFragment extends Fragment {
    private final String TAG = "Ashraful";
    private TabsAdapter mTabsAdapter;
    private int mainTabLength;
    private final String SAVE_POSITION = "position";
    private final String SAVE_TITLE = "title";
    private final String SAVE_TAG = "tag";
    private int mPosition = -1;
    private String mTitle, mTag;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c, Bundle save) {
        View v = inflater.inflate(R.layout.fragment_base, c, false);

        ActionBarActivity activity = (ActionBarActivity) getActivity();
        Resources rs = getResources();
        // title
        int position = getArguments().getInt(Config.ARG_POSITION);
        String title = rs.getStringArray(R.array.nav_array)[position];
        activity.setTitle(title);
        // initialize tabs
        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        pager = (ViewPager) v.findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(getActivity(), pager);
        pager.setAdapter(mTabsAdapter);
        tabs.setViewPager(pager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        String[] tabs = rs.getStringArray(R.array.tab_array)[position].split(",");
        String[] tags = rs.getStringArray(R.array.tag_array)[position].split(",");
        mainTabLength = tabs.length == tags.length ? tabs.length : Math.min(tabs.length, tags.length);

        for (int i = 0; i < mainTabLength; i++) {
            String tab = tabs[i];
            String tag = tags[i];
            Bundle bundle = new Bundle();
            bundle.putInt(Config.ARG_POSITION, position);
            bundle.putString(Config.ARG_TAG, tag);
            if (i == 0) mTabsAdapter.addTab(tab, tab, CategoryFragment.class, bundle);
            else mTabsAdapter.addTab(tab, tab, BaseTabFragment.class, bundle);
        }
        pager.setCurrentItem(1);
        if (save != null) {
            try {
                mPosition = save.getInt(SAVE_POSITION);
                mTitle = save.getString(SAVE_TITLE);
                mTag = save.getString(SAVE_TAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition != -1 && mTag != null && mTitle != null) {
            outState.putInt(SAVE_POSITION, mPosition);
            outState.putString(SAVE_TAG, mTag);
            outState.putString(SAVE_TITLE, mTitle);
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //newly added
    // tab helper class
    private class TabsAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private final FragmentActivity mContext;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        private final class TabInfo {
            @SuppressWarnings("unused")
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(FragmentActivity activity, ViewPager pager) {
            super(getChildFragmentManager());
            mContext = activity;
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(String tag, CharSequence label, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).tag;
        }


        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position             Position index of the first page currently being displayed.
         *                             Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        @Override
        public void onPageSelected(int position) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().show();
        }

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         * @see android.support.v4.view.ViewPager#SCROLL_STATE_IDLE
         * @see android.support.v4.view.ViewPager#SCROLL_STATE_DRAGGING
         * @see android.support.v4.view.ViewPager#SCROLL_STATE_SETTLING
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}