package com.betelguese.klassify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.ImageSlideAdapter;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.utils.Config;
import com.betelguese.klassify.utils.PageIndicator;
import com.widget.CustomTextView;

import java.util.ArrayList;

public class ProductDetailsActivity extends ActionBarActivity {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();
    ViewPager viewPager;
    PageIndicator pageIndicator;
    ImageButton favouriteButton;
    CustomTextView productNameTextView;
    CustomTextView productPriceTextView;
    CustomTextView productDetailsTextView;
    ImageButton callButton;
    private Handler handler;
    private boolean stopSliding;
    ImageButton smsButton;
    ImageButton shareButton;
    Product product;
    private ViewClickListeners mClickListeners;
    private Runnable animateViewPager;


    // pager
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initId();
        addListeners();
        startAnimaion();
    }

    private void startAnimaion() {
        if (product.getImage() != null) {
            viewPager.setAdapter(new ImageSlideAdapter(this, product.getImages(),true));
            pageIndicator.setViewPager(viewPager);
            runnable(product.getImages().size());
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    private void initId() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pageIndicator = (PageIndicator) findViewById(R.id.indicator);
        favouriteButton = (ImageButton) findViewById(R.id.favourite_button);
        productNameTextView = (CustomTextView) findViewById(R.id.product_name);
        productPriceTextView = (CustomTextView) findViewById(R.id.product_price);
        productDetailsTextView = (CustomTextView) findViewById(R.id.product_details);
        callButton = (ImageButton) findViewById(R.id.call_advert_user_button);
        smsButton = (ImageButton) findViewById(R.id.sms_advert_user_button);
        shareButton = (ImageButton) findViewById(R.id.share_button);
        product = getIntent().getParcelableExtra(Config.PRODUCT);
        productNameTextView.setText(product.getTitle());
        productDetailsTextView.setText(product.getDescription());
        productPriceTextView.setText("৳" + product.getPrice());
        initImageAnimation();
    }

    private void initImageAnimation() {
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager

                        if (product != null && product.getImages() != null && product.getImages().size() != 0) {
                            stopSliding = false;
                            runnable(product.getImages().size());
                            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            @Override
            public void run() {
                if (!stopSliding) {
                    if (viewPager.getCurrentItem() == size - 1) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    private void addListeners() {
        mClickListeners = new ViewClickListeners();
        favouriteButton.setOnClickListener(mClickListeners);
        callButton.setOnClickListener(mClickListeners);
        smsButton.setOnClickListener(mClickListeners);
        shareButton.setOnClickListener(mClickListeners);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class ViewClickListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.favourite_button) {
                Log.i(TAG, "favourite_button");
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ((ImageButton) v).setColorFilter(Color.argb(0xFF, 0x00, 0xC8, 0xF3));
                } else {
                    ((ImageButton) v).setColorFilter(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
                }
            } else if (v.getId() == R.id.call_advert_user_button) {
                Log.i(TAG, "call_advert_user_button");

            } else if (v.getId() == R.id.sms_advert_user_button) {
                Log.i(TAG, "sms_advert_user_button");

            } else if (v.getId() == R.id.share_button) {
                Log.i(TAG, "share_button");

            }
        }
    }
}
