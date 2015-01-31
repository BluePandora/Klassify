package com.betelguese.klassify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.ImageSlideAdapter;
import com.betelguese.klassify.utils.Config;
import com.betelguese.klassify.utils.PageIndicator;

import java.util.ArrayList;

/**
 * Created by ~Flash~ on 1/30/2015.
 */
public class ImageSliderActivity extends ActionBarActivity {
    private ViewPager viewPager;
    private PageIndicator pageIndicator;
    private Handler handler;
    private boolean stopSliding;
    private Runnable animateViewPager;
    // pager
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    private ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        initialize();
        startAnimaion();
    }

    private void initialize() {
        images = getIntent().getStringArrayListExtra(Config.ARG_IMAGES);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pageIndicator = (PageIndicator) findViewById(R.id.indicator);
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

                        if (images != null) {
                            stopSliding = false;
                            runnable(images.size());
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

    private void startAnimaion() {
        if (images != null) {
            viewPager.setAdapter(new ImageSlideAdapter(this, images));
            pageIndicator.setViewPager(viewPager);
            runnable(images.size());
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                Intent i = new Intent(getApplicationContext(),AndroidDownloadManagerActivity.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }


}
