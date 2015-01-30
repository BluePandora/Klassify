package com.betelguese.klassify.appdata;

import android.content.Context;
import android.os.AsyncTask;

import com.betelguese.klassify.json.CategoryJson;

import java.util.ArrayList;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategorySpinnerManager extends AsyncTask<String, Void, Void> {
    private ArrayList<Category> categories;
    private CategorySpinnerAdapter adapter;

    public CategorySpinnerManager(Context context, CategorySpinnerAdapter adapter) {
        this.adapter = adapter;
        this.categories = new ArrayList<Category>();
    }

    @Override
    protected Void doInBackground(String... url) {
        try {
            CategoryJson categoryJson = new CategoryJson(url[0]);
            categories = categoryJson.getCatagories();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (categories == null) {
            categories = new ArrayList<Category>();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        adapter.setData(categories);
    }
}
