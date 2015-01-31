package com.betelguese.klassify.appdata;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.betelguese.klassify.R;
import com.widget.CustomTextView;

import java.util.ArrayList;

public class FieldsAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private Activity activity;
    private LayoutInflater inflater;

    public FieldsAdapter(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.categoryName = (CustomTextView) convertView.findViewById(R.id.category_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String category = list.get(position);
        viewHolder.categoryName.setText(category);
        return convertView;
    }

    private static class ViewHolder {
        CustomTextView categoryName;
    }

    public void setData(ArrayList<String> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
