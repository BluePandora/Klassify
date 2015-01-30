package com.betelguese.klassify.appdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.betelguese.klassify.R;


/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class NavAdapter extends BaseAdapter {
    private String[] titles;

    public NavAdapter(String[] titles) {
        this.titles = titles;
    }

    @Override
    public int getCount() {
        if (titles != null)
            return titles.length;
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.drawer_list_item, parent, false);
        }
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(titles[position]);
        return v;
    }

    public String getTag(int position) {
        return titles[position];
    }
}