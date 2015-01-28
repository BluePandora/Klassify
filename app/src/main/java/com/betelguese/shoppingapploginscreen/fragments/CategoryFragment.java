package com.betelguese.shoppingapploginscreen.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.betelguese.shoppingapploginscreen.R;
import com.betelguese.shoppingapploginscreen.activities.ProductListActivity;
import com.betelguese.shoppingapploginscreen.appdata.NavAdapter;
import com.betelguese.shoppingapploginscreen.utils.Config;


/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategoryFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private final String TAG = "Ashraful";
    private NavAdapter adapter;
    private ListView listView;
    private String tag;
    private int navPosition;
    private String[] categories;
    private ActionBar actionBar;
    private int mLastFirstVisibleItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c, Bundle save) {
        View v = inflater.inflate(R.layout.category_layout, c, false);
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        categories = getResources().getStringArray(R.array.cat_array);
        listView = (ListView) v.findViewById(R.id.list);
        adapter = new NavAdapter(categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        return v;
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
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(Config.ARG_TAG, adapter.getTag(position));
        startActivity(intent);
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     * {@link android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)}.
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getId() == listView.getId()) {
            int currentFirstVisibleItem = listView.getFirstVisiblePosition();
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

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((ActionBarActivity) activity).getSupportActionBar().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
