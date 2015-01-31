package com.betelguese.klassify.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.Category;
import com.betelguese.klassify.appdata.CategorySpinnerAdapter;
import com.betelguese.klassify.appdata.CategorySpinnerManager;
import com.betelguese.klassify.appdata.FieldsAdapter;
import com.betelguese.klassify.appdata.SubCategory;
import com.betelguese.klassify.appdata.SubCategorySpinnerAdapter;
import com.widget.CustomButton;
import com.widget.CustomEditText;

import org.json.JSONObject;

public class ProductAdvertActivity extends ActionBarActivity implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener {

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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_add);
        init();
        initListeners();
        getCatagories("http://www.mocky.io/v2/54cc1d3196d6b2091a431fd5");
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
            subAdapter.setData(((Category) ((CategorySpinnerAdapter) parent.getAdapter()).getItem(position)).getSubCategories());
            fieldsAdapter.setData(((SubCategory) subCatagorySpinner.getAdapter().getItem(subCatagorySpinner.getSelectedItemPosition()==-1?0:subCatagorySpinner.getSelectedItemPosition())).getFields());
        }
        if (parent.equals(subCatagorySpinner)) {
            fieldsAdapter.setData(((SubCategory) ((SubCategorySpinnerAdapter) parent.getAdapter()).getItem(position)).getFields());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
