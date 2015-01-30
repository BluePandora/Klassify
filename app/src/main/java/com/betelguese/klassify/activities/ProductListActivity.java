package com.betelguese.klassify.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.appdata.ProductAdapter;
import com.betelguese.klassify.appdata.ProductManager;
import com.betelguese.klassify.utils.Config;
import com.betelguese.klassify.utils.OnMessageListener;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;

public class ProductListActivity extends ActionBarActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnMessageListener {
    private final String TAG = "Ashraful";
    private ProductAdapter adapter;
    private StaggeredGridView mGridView;
    private String tag;
    private int howMany = 25;
    private int min = 6;
    private SwipeRefreshLayout swipeContainer;
    private String url;
    private final String SAVE_VALUE_KEY = "save";
    private int mLastFirstVisibleItem;
    private ActionBar actionBar;
    private OnMessageListener listerner;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.activity_product_list_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        initialize(getIntent(), save);
    }

    private void initialize(Intent intent, Bundle save) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            url = getResources().getString(R.string.url);
            tag = intent.getStringExtra(SearchManager.QUERY);
        } else {
            url = getResources().getString(R.string.url);
            tag = intent.getStringExtra(Config.ARG_TAG);
        }
        setTitle(tag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        if (save != null) {
            adapter.initMore();
            ArrayList<Product> news = save.getParcelableArrayList(SAVE_VALUE_KEY);
            if (news == null)
                news = new ArrayList<Product>();
            adapter.setData(news);
            adapter.invalidate();
        } else {
            displayNews(-1, Config.TASK_START);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initialize(intent, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null)
            outState.putParcelableArrayList(SAVE_VALUE_KEY, adapter.getData());
    }

    private void initView() {
        SwipeRefreshLayout empty = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_empty);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.load);
        mGridView = (StaggeredGridView) findViewById(R.id.list);
        LayoutInflater layoutInflater = getLayoutInflater();

        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        mGridView.addFooterView(footer);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeContainer.setOnRefreshListener(this);
        adapter = new ProductAdapter(this, progressBar, empty, 0, swipeContainer, listerner);
        mGridView.setAdapter(adapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        empty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!adapter.mHasRequestedMore) {
                    if (adapter.getCount() < min)
                        howMany += min;
                    displayNews(-1, Config.TASK_START);
                }
            }
        });
    }

    private void displayNews(int pointer, int task) {
        ProductManager manager = new ProductManager(this, adapter, task);
        //manager.execute(url + "?tag=" + tag + "&pointer=" + pointer + "&howMany=" + howMany);
        manager.execute(url);
    }


    private void onLoadMoreItems() {
        displayNews(adapter.getPointer(), Config.TASK_MORE);
    }

    @Override
    public void onRefresh() {
        if (!adapter.mHasRequestedMore) {
            if (adapter.getCount() < min)
                howMany += min;
            displayNews(-1, Config.TASK_REFRESH);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!adapter.mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                adapter.mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
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
    public void onReceiveMessage(Bundle bundle) {
        if (bundle != null && listerner != null) {
            listerner.onReceiveMessage(bundle);
        }
    }

    public void refreshData(String tag) {
        this.tag = tag;
        displayNews(-1, Config.TASK_START);
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
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }
}
