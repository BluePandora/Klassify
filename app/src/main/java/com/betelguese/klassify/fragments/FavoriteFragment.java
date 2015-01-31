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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betelguese.klassify.R;
import com.betelguese.klassify.activities.ProductDetailsActivity;
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

public class FavoriteFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, OnMessageListener {
    private final String TAG = "Ashraful";
    protected ProductAdapter adapter;
    private StaggeredGridView mGridView;
    protected String tag;
    private int navPosition;
    private SwipeRefreshLayout swipeContainer;
    private final String SAVE_VALUE_KEY = "save";
    private int mLastFirstVisibleItem;
    private ActionBar actionBar;
    private OnMessageListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c, Bundle save) {
        View v = inflater.inflate(R.layout.product_list_layout, c, false);
        initView(v, save);
        if (save != null) {
            adapter.initMore();
            ArrayList<Product> news = save.getParcelableArrayList(SAVE_VALUE_KEY);
            if (news == null)
                news = new ArrayList<Product>();
            adapter.setData(news);
            adapter.invalidate();
        } else {
            displayNews();
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
        mGridView = (StaggeredGridView) v.findViewById(R.id.list);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        adapter = new ProductAdapter(getActivity(), progressBar, empty, navPosition, swipeContainer, listener);
        mGridView.setAdapter(adapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        adapter.isFavoriteFragment(true);
    }

    protected void displayNews() {
        adapter.mHasRequestedMore = true;
        ProductManager manager = new ProductManager(getActivity(), adapter, Config.TASK_FAVORITE);
        manager.execute(tag);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (view.getId() == mGridView.getId()) {
            int currentFirstVisibleItem = mGridView.getFirstVisiblePosition();
            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
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

    @Override
    public void onReceiveMessage(Bundle bundle) {
        if (bundle != null && listener != null) {
            listener.onReceiveMessage(bundle);
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
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra(Config.PRODUCT, adapter.getData(position));
        intent.putExtra(Config.ARG_POSITION, position);
        intent.putExtra(Config.ARG_NAV_POSITION, navPosition);
        startActivityForResult(intent, Config.REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra(Config.ARG_POSITION, -1);
                boolean isFavorite = data.getBooleanExtra(Config.ARG_IS_FAVORITE, false);
                if (position != -1) {
                    adapter.setFavorite(position, isFavorite);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
