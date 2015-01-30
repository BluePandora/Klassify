package com.betelguese.klassify.json;

import com.betelguese.klassify.appdata.Category;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.appdata.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class CategoryJson {
    private final String ID = "category_id";
    private final String TITLE = "title";
    private final String SUB_ID = "sub_id";
    private final String SUB_TITLE = "sub_title";
    private final String FIELD = "field";
    private final String FIELD_NAME = "name";
    private final String SUBCATEGORY = "subcategory";
    private String link;

    public CategoryJson(String link) {
        this.link = link;
    }

    public ArrayList<Category> getCatagories() {
        ArrayList<Category> categories = new ArrayList<>();
        JSONParser parser = null;
        JSONArray productList;
        try {
            parser = new JSONParser();
            productList = parser.getJSONArrayFromUrl(link);
            for (int i = 0; i < productList.length(); i++) {
                try {
                    JSONObject newsItem = productList.getJSONObject(i);
                    String id = getData(newsItem, ID);
                    String title = getData(newsItem, TITLE);
                    ArrayList<SubCategory> subCategories = getSubCategories(newsItem, SUBCATEGORY);
                    categories.add(new Category(id, title, subCategories));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (categories == null) {
            categories = new ArrayList<Category>();
        }
        return categories;
    }

    private ArrayList<SubCategory> getSubCategories(JSONObject newsItem, String key) {
        ArrayList<SubCategory> subCategories = new ArrayList<>();
        try {
            JSONArray array = newsItem.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                String subId = getData(item, SUB_ID);
                String subTitle = getData(item, SUB_TITLE);
                ArrayList<String> fields = getFields(item, FIELD);
                subCategories.add(new SubCategory(subId, subTitle, fields));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (subCategories == null) {
            subCategories = new ArrayList<SubCategory>();
        }
        return subCategories;
    }

    private ArrayList<String> getFields(JSONObject item, String key) {
        ArrayList<String> fields = new ArrayList<>();
        try {
            JSONArray array = item.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                fields.add(array.getJSONObject(i).getString(FIELD_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fields == null) {
            fields = new ArrayList<String>();
        }
        return fields;
    }


    private String getData(JSONObject jdata, String key) {
        String data = "";
        try {
            data = jdata.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}