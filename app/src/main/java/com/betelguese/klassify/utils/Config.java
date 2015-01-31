package com.betelguese.klassify.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.AppController;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.connection.AlertDialogForAnything;
import com.betelguese.klassify.model.UserInfo;
import com.database.DatabaseOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class Config {
    public static final String ARG_POSITION = "position";
    public static final String ARG_TITLE = "title";
    public static final String ARG_TAG = "tag";
    public static final int TASK_START = 0;
    public static final int TASK_REFRESH = 1;
    public static final int TASK_MORE = 2;

    public static String TABLE_FAVOURITE = "favourite";
    public static String TABLE_IMAGES = "images";
    public static String FavTableItem[] = { "productId","title","description","images","email","createdDate","price" };
    public static String ImageItem[] = { "image_id","imageUrl" };


    public static final String ARG_CATEGORY = "category";
    public static final String ARG_CATEGORY_ITEM = "item_category";
    public static final String PRODUCT = "product";
    public static final String ARG_IMAGES = "images";
    public static final int REQUEST_CODE = 1;
    public static final String ARG_IS_FAVORITE = "is_favorite";
    public static final String ARG_NAV_POSITION = "nav_position";
    public static final int TASK_FAVORITE = 3;

    public static final UserInfo userInfo=null;


    public static void volleyRequest(Context context) {
        String tag_string_req = "string_req";
        String url = "http://www.mocky.io/v2/54cb2aae96d6b2a703431eef" ;

        //Response.Listener<String> ,Response.ErrorListener
        final DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(context);
        final ArrayList<Product> products = dbOpenHelper.getAllfromMyProductTable();
        for(int i=0;i<products.size();i++) {
            final Product product = products.get(i);
            StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("", response.toString());
                    try {
                        String responseString = new JSONObject(response).getString("status");
                        if (responseString.equals("success")) {

                            dbOpenHelper.deleteFromMyProductTable(product);

                        } else {


                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Log", "Error: " + error.getMessage());

                }
            }) {
                @Override
                protected HashMap<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("product_name", product.getTitle());
                    params.put("price", String.valueOf(product.getPrice()));
                    params.put("category",product.getCategory());
                    params.put("subcategory", product.getSubCategory());
                    params.put("field", product.getField());
                    params.put("product_detail", product.getDescription());
                    JSONArray array = new JSONArray(product.getImages());
                    params.put("images", array.toString());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }
}
