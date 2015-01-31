package com.betelguese.klassify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.ImageSlideAdapter;
import com.betelguese.klassify.appdata.Product;
import com.betelguese.klassify.utils.Config;
import com.betelguese.klassify.utils.PageIndicator;
import com.database.DatabaseOpenHelper;
import com.widget.CustomTextView;

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
    private int navPosition;


    // pager
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    private DatabaseOpenHelper db;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseOpenHelper(this);
        initId();
        addListeners();
        startAnimaion();
    }

    private void startAnimaion() {
        if (product.getImage() != null) {
            viewPager.setAdapter(new ImageSlideAdapter(this, product.getImages(), true));
            pageIndicator.setViewPager(viewPager);
            runnable(product.getImages().size());
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
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
        position = getIntent().getIntExtra(Config.ARG_POSITION, -1);
        navPosition = getIntent().getIntExtra(Config.ARG_NAV_POSITION, -1);
        productNameTextView.setText(product.getTitle());
        productDetailsTextView.setText(product.getDescription());
        productPriceTextView.setText("৳" + product.getPrice());
        favouriteButton.setSelected(product.isFavorite());
        if (favouriteButton.isSelected()) {
            favouriteButton.setColorFilter(Color.argb(0xFF, 0x00, 0xC8, 0xF3));
        } else {
            favouriteButton.setColorFilter(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
        }
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
        if (id == android.R.id.home) {
            finishActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void finishActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Config.ARG_IS_FAVORITE, product.isFavorite());
        returnIntent.putExtra(Config.ARG_POSITION, position);
        returnIntent.putExtra(Config.ARG_NAV_POSITION, navPosition);
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    private class ViewClickListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.favourite_button) {
                Log.i(TAG, "favourite_button");
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ((ImageButton) v).setColorFilter(Color.argb(0xFF, 0x00, 0xC8, 0xF3));
                    db.insertFavTable(product);
                } else {
                    ((ImageButton) v).setColorFilter(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
                    db.deleteFromFavTable(product);
                }
                product.setFavorite(v.isSelected());
            } else if (v.getId() == R.id.call_advert_user_button) {
                Log.i(TAG, "call_advert_user_button");
                makecall(product.getPhone());
            } else if (v.getId() == R.id.sms_advert_user_button) {
                Uri uri = Uri.parse("smsto:" + product.getPhone());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Hi want to buy your " + product.getTitle());
                startActivity(it);
            } else if (v.getId() == R.id.share_button) {
                Log.i(TAG, "share_button");
                share_add();
            }
        }

        private void makecall(String number) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Call failed", Toast.LENGTH_LONG);
            }
        }
    }

    private void share_add() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Sale Sale Sale ");
        share.putExtra(Intent.EXTRA_TEXT, "I want to sale my bike proce is tut tut " + "\n" + "http://sustcse10.net/ashraful/emarket/uploads/a.jpg");
        startActivity(Intent.createChooser(share, "Share link!"));
    }
}
