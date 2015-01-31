package com.betelguese.klassify.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.betelguese.klassify.R;
import com.betelguese.klassify.activities.util.Action;
import com.betelguese.klassify.activities.util.CustomGallery;
import com.betelguese.klassify.appdata.AppController;
import com.betelguese.klassify.appdata.Category;
import com.betelguese.klassify.appdata.CategorySpinnerAdapter;
import com.betelguese.klassify.appdata.CategorySpinnerManager;
import com.betelguese.klassify.appdata.FieldsAdapter;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.appdata.SubCategory;
import com.betelguese.klassify.appdata.SubCategorySpinnerAdapter;
import com.betelguese.klassify.connection.AlertDialogForAnything;
import com.betelguese.klassify.connection.ConnectionDetector;
import com.betelguese.klassify.utils.Config;
import com.database.DatabaseOpenHelper;
import com.widget.CustomButton;
import com.widget.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductAdvertActivity extends ActionBarActivity implements Response.Listener<String>, Response.ErrorListener, AdapterView.OnItemSelectedListener {

    ConnectionDetector cd;
    DatabaseOpenHelper dbOpenHelper;
    private JsonObjectRequest mJsonObjectRequest;
    private CategorySpinnerAdapter adapter;
    private SubCategorySpinnerAdapter subAdapter;
    private FieldsAdapter fieldsAdapter;
    private RelativeLayout addProductImage;
    private ImageView singleProductImage;
    private Spinner catagorySpinner;
    private Spinner subCatagorySpinner;
    private Spinner FieldsSpinner;
    private CustomEditText productNameEditText;
    private CustomEditText productPriceEditText;
    private CustomEditText productDetailsEditText;
    private CustomButton addProductButton;
    private ViewListeners mViewListeners;
    ArrayList<String> imagesPath;
    Product productItem;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        init();
        initListeners();
        getCatagories(getString(R.string.url_category));
    }

    private void getCatagories(String tag) {
        CategorySpinnerManager manager = new CategorySpinnerManager(this, adapter);
        manager.execute(tag);
    }

    private void initListeners() {
        mViewListeners = new ViewListeners();
        addProductImage.setOnClickListener(mViewListeners);
        addProductButton.setOnClickListener(mViewListeners);
    }

    private void init() {
        cd = new ConnectionDetector(this);
        dbOpenHelper = new DatabaseOpenHelper(this);

        catagorySpinner = (Spinner) findViewById(R.id.catagory_spinner);
        subCatagorySpinner = (Spinner) findViewById(R.id.subcatagory_spinner);
        FieldsSpinner = (Spinner) findViewById(R.id.fields_spinner);
        productNameEditText = (CustomEditText) findViewById(R.id.product_name_edit_text);
        productPriceEditText = (CustomEditText) findViewById(R.id.product_price_edit_text);
        productDetailsEditText = (CustomEditText) findViewById(R.id.product_details_edit_text);
        addProductImage = (RelativeLayout) findViewById(R.id.add_image);
        singleProductImage = (ImageView) findViewById(R.id.product_image);
        addProductButton = (CustomButton) findViewById(R.id.add_product_button);
        adapter = new CategorySpinnerAdapter(this);
        subAdapter = new SubCategorySpinnerAdapter(this);
        fieldsAdapter = new FieldsAdapter(this);
        catagorySpinner.setAdapter(adapter);
        subCatagorySpinner.setAdapter(subAdapter);
        catagorySpinner.setOnItemSelectedListener(this);
        subCatagorySpinner.setOnItemSelectedListener(this);
        FieldsSpinner.setAdapter(fieldsAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(catagorySpinner)) {
            subAdapter.setData(((Category) ( parent.getAdapter()).getItem(position)).getSubCategories());
            fieldsAdapter.setData(((SubCategory) subCatagorySpinner.getAdapter().getItem(subCatagorySpinner.getSelectedItemPosition() == -1 ? 0 : subCatagorySpinner.getSelectedItemPosition())).getFields());
        }
        if (parent.equals(subCatagorySpinner)) {
            fieldsAdapter.setData(((SubCategory) (parent.getAdapter()).getItem(position)).getFields());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class ViewListeners implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.add_image) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }else if(v.getId() == R.id.add_product_button){
                addProductButtonActivity();
            }

        }
    }

    private void addProductButtonActivity() {
        if(cd.isConnectingToInternet()){
            volleyRequest();
        }else{
            //String productId, String title, String description, ArrayList<String> images, String phone, String email, String createdDate, double price, boolean isFavourite) {

            productItem = new Product(String.valueOf(System.currentTimeMillis()),productDetailsEditText.getText().toString(),productDetailsEditText.getText().toString(),imagesPath, Config.userInfo.getMobile_number(),Config.userInfo.getEmail(), String.valueOf(System.currentTimeMillis()) , Double.parseDouble(productPriceEditText.getText().toString()) ,false);
            dbOpenHelper.insertMyProductTable(productItem);
            cd.showAlertDialogToNetworkConnection(this,"Alert", "No Internet Connection",false);
        }

    }
    ProgressDialog pDialog;
    private void volleyRequest() {
        String tag_string_req = "string_req";

        String url = getString(R.string.post_product_url);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Progrssing...");
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, url, this , this ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("product_name", productDetailsEditText.getText().toString().trim());
                params.put("price", productPriceEditText.getText().toString().trim());
                params.put("category", catagorySpinner.getSelectedItem().toString());
                params.put("subcategory", subCatagorySpinner.getSelectedItem().toString());
                params.put("field", FieldsSpinner.getSelectedItem().toString());
                params.put("product_detail", productDetailsEditText.getText().toString().trim());
                JSONArray array = new JSONArray(imagesPath);
                params.put("images",array.toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        pDialog.hide();
        productItem = new Product(String.valueOf(System.currentTimeMillis()),productDetailsEditText.getText().toString(),productDetailsEditText.getText().toString(),imagesPath, Config.userInfo.getMobile_number(),Config.userInfo.getEmail(), String.valueOf(System.currentTimeMillis()) , Double.parseDouble(productPriceEditText.getText().toString()) ,false);
        dbOpenHelper.insertMyProductTable(productItem);
    }

    @Override
    public void onResponse(String response) {
        try {
            String responseString = new JSONObject(response).getString("status");
            if (responseString.equals("success")) {

                Toast.makeText(ProductAdvertActivity.this, "success", Toast.LENGTH_SHORT).show();
                pDialog.hide();

            } else {
                AlertDialogForAnything.showAlertDialogWhenComplte(ProductAdvertActivity.this, "Fail", "Fail to sign up.", false);
                pDialog.hide();
                productItem = new Product(String.valueOf(System.currentTimeMillis()),productDetailsEditText.getText().toString(),productDetailsEditText.getText().toString(),imagesPath, Config.userInfo.getMobile_number(),Config.userInfo.getEmail(), String.valueOf(System.currentTimeMillis()) , Double.parseDouble(productPriceEditText.getText().toString()) ,false);
                dbOpenHelper.insertMyProductTable(productItem);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        pDialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            imagesPath = new ArrayList<String>();
            for (String string : all_path) {
                imagesPath.add(string);
            }

            if(all_path.length>0){
                File file =new File(all_path[0]);
                Uri uri = Uri.fromFile(file);
                singleProductImage.setImageURI(uri);
                singleProductImage.setColorFilter(null);
            }



        }
    }
}

