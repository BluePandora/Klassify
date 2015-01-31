package com.betelguese.klassify.appdata;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.betelguese.klassify.R;
import com.betelguese.klassify.utils.OnMessageListener;
import com.database.DatabaseOpenHelper;
import com.fedorvlasov.lazylist.ImageLoader;

import java.util.ArrayList;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class ProductAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<Product> list = new ArrayList<Product>();
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
    private DatabaseOpenHelper db;


    public ProductAdapter(Context context, ProgressBar progressBar, SwipeRefreshLayout empty, int navPosition, SwipeRefreshLayout view, OnMessageListener listener) {
        this.progressBar = progressBar;
        this.empty = empty;
        this.context = context;
        this.pointer = -1;
        this.listener = listener;
        this.navPosition = navPosition;
        this.view = view;
        imageLoader = new ImageLoader(context);
        check = new SparseBooleanArray();
        db = new DatabaseOpenHelper(context);
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

    public void setData(ArrayList<Product> list) {
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

    public void add(Product data) {
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
            v = inflater.inflate(R.layout.product_list_item, parent, false);
        }
        Product data = list.get(position);

        TextView title = (TextView) v.findViewById(R.id.product_name);
        title.setText(String.valueOf(data.getTitle()));

        TextView price = (TextView) v.findViewById(R.id.product_price);
        price.setText(String.valueOf("৳" + data.getPrice()));

        ImageView image = (ImageView) v.findViewById(R.id.image);
        imageLoader.DisplayImage(data.getImage(), image);

        ImageButton save = (ImageButton) v.findViewById(R.id.favorites);
        save.setSelected(data.isFavorite());
        updateFavorite(save);
        save.setTag(position);
        save.setOnClickListener(this);
        return v;
    }


    public String getDataID(int position) {
        return list.get(position).getProductId();
    }

    public String getUrl(int position) {
        return null;//list.get(position).getImage();
    }

    public int getPointer() {
        return pointer;
    }


    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public void setMoreData(ArrayList<Product> categories) {
        if (categories != null && categories.size() != 0) {
            if (list == null)
                list = new ArrayList<Product>();
            list.addAll(categories);
        }
    }

    public void initMore() {
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
    }

    public void setRefreshData(ArrayList<Product> list) {
        if (this.list == null || this.list.size() == 0)
            this.list = list;
        else if (list != null && list.size() != 0) {
            try {
                String firstId = list.get(0).getProductId();
                boolean isFound = false;
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (firstId.equals(list.get(i).getProductId())) {
                        isFound = true;
                    } else if (isFound) {
                        this.list.set(0, list.get(i));
                    }
                }

                if (!isFound) {
                    this.list.addAll(0, list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Product> getData() {
        return list;
    }

    private void updateFavorite(View v) {
        if (v.isSelected()) {
            ((ImageButton) v).setColorFilter(Color.argb(0xFF, 0x42, 0xA5, 0xF5));
        } else {
            ((ImageButton) v).setColorFilter(null);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (v.getId() == R.id.favorites) {
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                db.insertFavTable(list.get(position));
            } else {
                db.deleteFromFavTable(list.get(position));
            }
            setFavorite(position, v.isSelected());
            updateFavorite(v);
        }
    }

    public void setFavorite(int position, boolean isFavorite) {
        try {
            list.get(position).setFavorite(isFavorite);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.isFavoriteFragment && !isFavorite) {
            list.remove(position);
            this.notifyDataSetChanged();
        }
    }


    public void initRefresh() {
        progressBar.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
    }

    public Product getData(int position) {
        return list.get(position);
    }


    boolean isFavoriteFragment;

    public void isFavoriteFragment(boolean isFavoriteFragment) {
        this.isFavoriteFragment = isFavoriteFragment;
    }

    public void sortByField(int position) {
        switch (position) {
            case 0:
                sortByMostRecent();
                break;
            case 1:
                sortByPricehighToLow();
                break;
            case 2:
                sortByPriceLowToHigh();
                break;
            case 3:
                sortByMostViewed();
                break;
            default:
                break;
        }
        notifyDataSetChanged();
    }

    private void sortByMostViewed() {

    }

    private void sortByPriceLowToHigh() {

    }

    private void sortByPricehighToLow() {

    }

    private void sortByMostRecent() {

    }

//    private void sendMessage(TagPair tagPair) {
//        if (listener != null) {
//            Bundle bundle = new Bundle();
//            bundle.putInt(Config.ARG_TYPE, Config.TYPE_TAG);
//            bundle.putString(Config.ARG_KEY, tagPair.getKey());
//            bundle.putString(Config.ARG_VALUE, tagPair.getValue());
//            bundle.putInt(Config.ARG_POSITION, navPosition);
//            listener.onReceiveMessage(bundle);
//        }
//    }
}
