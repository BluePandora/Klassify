package com.betelguese.klassify.appdata;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.betelguese.klassify.R;
import com.betelguese.klassify.utils.OnMessageListener;
import com.fedorvlasov.lazylist.ImageLoader;

import java.util.ArrayList;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategoryAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<Category> list = new ArrayList<Category>();
    private ProgressBar progressBar;
    private SwipeRefreshLayout empty;
    private ImageLoader imageLoader;
    private int pointer;
    private SparseBooleanArray check;
    private Context context;
    private int navPosition;
    private OnMessageListener listener;
    private SwipeRefreshLayout view;
    public boolean mHasRequestedMore;


    public CategoryAdapter(Context context, ProgressBar progressBar, SwipeRefreshLayout empty, int navPosition, SwipeRefreshLayout view, OnMessageListener listener) {
        this.progressBar = progressBar;
        this.empty = empty;
        this.context = context;
        this.pointer = -1;
        this.listener = listener;
        this.navPosition = navPosition;
        this.view = view;
        imageLoader = new ImageLoader(context);
        check = new SparseBooleanArray();
    }

    public void initLoad() {
        if (list != null) {
            list.clear();
            invalidate();
        }
        view.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    public void setData(ArrayList<Category> list) {
        this.list = list;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Category data) {
        list.add(data);
    }

    public void invalidate() {
        progressBar.setVisibility(View.GONE);
        if (list.size() < 1) {
            empty.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }
        view.setRefreshing(false);
        empty.setRefreshing(false);
        mHasRequestedMore = false;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.drawer_list_item, parent, false);
        }
        Category data = list.get(position);

        TextView name = (TextView) v.findViewById(R.id.title);
        name.setText(data.getTitle());

        return v;
    }


    public String getDataID(int position) {
        return list.get(position).getId();
    }


    public int getPointer() {
        return pointer;
    }


    public void setPointer(int pointer) {
        this.pointer = pointer;
    }


    public void initMore() {
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
    }

    public ArrayList<Category> getData() {
        return list;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
//        if (v.getId() == R.id.buy) {
//            Log.e("Ashraful", "position:" + position);
//        } else if (v.getId() == R.id.save) {
//            Log.e("Ashraful", "position:" + position);
//        }
    }

    public void initRefresh() {
        progressBar.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
    }

    public Category getData(int position) {
        return list.get(position);
    }
}
