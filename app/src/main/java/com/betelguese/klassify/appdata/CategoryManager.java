package com.betelguese.klassify.appdata;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.betelguese.klassify.activities.LogInActivity;
import com.betelguese.klassify.json.CategoryJson;
import com.betelguese.klassify.json.ProductJson;
import com.betelguese.klassify.utils.Config;

import java.util.ArrayList;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategoryManager extends AsyncTask<String, Void, Void> {
    private ArrayList<Category> categories;
    private CategoryAdapter adapter;
    private int task;

    public CategoryManager(Context context, CategoryAdapter adapter, int task) {
        this.adapter = adapter;
        this.task = task;
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
        adapter.invalidate();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        switch (task) {
            case Config.TASK_MORE:
                adapter.initMore();
                break;
            case Config.TASK_REFRESH:
                adapter.initRefresh();
                break;
            default:
                adapter.initLoad();
                break;
        }

    }

}
