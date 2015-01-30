package com.betelguese.klassify.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betelguese.klassify.R;
import com.betelguese.klassify.activities.CategoryHome;
import com.betelguese.klassify.activities.ProductListActivity;
import com.betelguese.klassify.appdata.Category;
import com.betelguese.klassify.appdata.CategoryAdapter;
import com.betelguese.klassify.appdata.CategoryManager;
import com.betelguese.klassify.appdata.NavAdapter;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.appdata.ProductAdapter;
import com.betelguese.klassify.appdata.ProductManager;
import com.betelguese.klassify.utils.Config;
import com.betelguese.klassify.utils.OnMessageListener;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;


/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategoryFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "Ashraful";
    private CategoryAdapter adapter;
    private ListView listView;
    private String tag;
    private int navPosition;
    private SwipeRefreshLayout swipeContainer;
    private final String SAVE_VALUE_KEY = "save";
    private int mLastFirstVisibleItem;
    private ActionBar actionBar;
    private OnMessageListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c, Bundle save) {
        View v = inflater.inflate(R.layout.category_layout, c, false);
        initView(v, save);
        if (save != null) {
            adapter.initMore();
            ArrayList<Category> categories = save.getParcelableArrayList(SAVE_VALUE_KEY);
            if (categories == null)
                categories = new ArrayList<Category>();
            adapter.setData(categories);
            adapter.invalidate();
        } else {
            displayNews(Config.TASK_START);
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null)
            outState.putParcelableArrayList(SAVE_VALUE_KEY, adapter.getData());
    }

    private void initView(View v, Bundle save) {
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        navPosition = getArguments().getInt(Config.ARG_POSITION);
        if (tag == null)
            tag = getArguments().getString(Config.ARG_TAG);
        SwipeRefreshLayout empty = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_empty);
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.load);
        listView = (ListView) v.findViewById(R.id.list);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeContainer.setOnRefreshListener(this);
        adapter = new CategoryAdapter(getActivity(), progressBar, empty, navPosition, swipeContainer, listener);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        empty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!adapter.mHasRequestedMore) {
                    displayNews(Config.TASK_START);
                }
            }
        });
    }

    private void displayNews(int task) {
        adapter.mHasRequestedMore = true;
        CategoryManager manager = new CategoryManager(getActivity(), adapter, task);
        manager.execute(tag);
    }


    @Override
    public void onRefresh() {
        if (!adapter.mHasRequestedMore) {
            displayNews(Config.TASK_REFRESH);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // our handling
        if (view.getId() == listView.getId()) {
            int currentFirstVisibleItem = listView.getFirstVisiblePosition();
            if (currentFirstVisibleItem - 2 > mLastFirstVisibleItem) {
                if (actionBar.isShowing()) {
                    actionBar.hide();
                }
            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                if (!actionBar.isShowing()) {
                    actionBar.show();
                }
            }
            mLastFirstVisibleItem = currentFirstVisibleItem;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnMessageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMessageListener");
        }
    }


    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CategoryHome.class);
        CategoryHome.categories = adapter.getData();
        intent.putExtra(Config.ARG_POSITION,position);
        startActivity(intent);
    }
}
