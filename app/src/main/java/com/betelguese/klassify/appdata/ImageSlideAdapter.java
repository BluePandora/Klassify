package com.betelguese.klassify.appdata;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.betelguese.klassify.R;
import com.betelguese.klassify.activities.ImageSliderActivity;
import com.betelguese.klassify.utils.Config;
import com.fedorvlasov.lazylist.ImageLoader;

public class ImageSlideAdapter extends PagerAdapter implements View.OnClickListener {
    private ImageLoader imageLoader;
    private Activity activity;
    private ArrayList<String> imageUrl;
    private boolean isClickAble;

    public ImageSlideAdapter(Activity activity, ArrayList<String> imageUrl) {
        this(activity, imageUrl, false);
    }

    public ImageSlideAdapter(Activity activity, ArrayList<String> imageUrl, boolean isClickAble) {
        this.activity = activity;
        this.imageUrl = imageUrl;
        imageLoader = new ImageLoader(activity);
        this.isClickAble = isClickAble;
    }

    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_image, container, false);

        ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);
        imageLoader.DisplayImage(imageUrl.get(position), mImageView);
        if (isClickAble) {
            mImageView.setTag(position);
            mImageView.setOnClickListener(this);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        Intent intent = new Intent(activity, ImageSliderActivity.class);
        intent.putStringArrayListExtra(Config.ARG_IMAGES, imageUrl);
        activity.startActivity(intent);
    }
}