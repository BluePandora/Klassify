package com.betelguese.klassify.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.CategorySpinnerAdapter;
import com.betelguese.klassify.appdata.CategorySpinnerManager;
import com.widget.CustomButton;
import com.widget.CustomEditText;

import org.json.JSONObject;

public class ProductAdvertActivity extends ActionBarActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private JsonObjectRequest mJsonObjectRequest;
    CategorySpinnerAdapter adapter;
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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        init();
        initListeners();
        getCatagories(null);
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
        catagorySpinner = (Spinner) findViewById(R.id.catagory_spinner);
        subCatagorySpinner = (Spinner) findViewById(R.id.subcatagory_spinner);
        FieldsSpinner = (Spinner) findViewById(R.id.fields_spinner);
        productNameEditText = (CustomEditText) findViewById(R.id.product_name);
        productPriceEditText = (CustomEditText) findViewById(R.id.product_price);
        productDetailsEditText = (CustomEditText) findViewById(R.id.product_details_edit_text);
        addProductImage = (RelativeLayout) findViewById(R.id.add_image);
        singleProductImage = (ImageView) findViewById(R.id.product_image);
        addProductButton = (CustomButton) findViewById(R.id.add_product_button);
        adapter = new CategorySpinnerAdapter();
        catagorySpinner.setAdapter(adapter);
    }

    private class ViewListeners implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(JSONObject jsonObject) {

    }
}
