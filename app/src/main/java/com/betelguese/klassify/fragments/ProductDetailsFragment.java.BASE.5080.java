package com.betelguese.klassify.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.betelguese.klassify.R;
import com.widget.CustomTextView;


public class ProductDetailsFragment extends Fragment {

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    private View rootView;
    private ViewHolder viewHolder;
    private ViewClickListeners mClickListeners;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.product_detail, container, false);
            viewHolder = new ViewHolder();
            viewHolder.productImageView = (ImageView) rootView.findViewById(R.id.product_image);
            viewHolder.favouriteButton = (ImageButton) rootView.findViewById(R.id.favourite_button);
            viewHolder.productNameTextView = (CustomTextView) rootView.findViewById(R.id.product_name);
            viewHolder.productPriceTextView = (CustomTextView) rootView.findViewById(R.id.product_price);
            viewHolder.productDetailsTextView = (CustomTextView) rootView.findViewById(R.id.product_details);
            viewHolder.callButton = (ImageButton) rootView.findViewById(R.id.call_advert_user_button);
            viewHolder.smsButton = (ImageButton) rootView.findViewById(R.id.sms_advert_user_button);
            viewHolder.shareButton = (ImageButton) rootView.findViewById(R.id.share_button);
            rootView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rootView.getTag();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mClickListeners = new ViewClickListeners();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        viewHolder.favouriteButton.setOnClickListener(mClickListeners);
        viewHolder.callButton.setOnClickListener(mClickListeners);
        viewHolder.smsButton.setOnClickListener(mClickListeners);
        viewHolder.shareButton.setOnClickListener(mClickListeners);
    }

    private class ViewClickListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.favourite_button) {

            } else if (v.getId() == R.id.call_advert_user_button) {

            } else if (v.getId() == R.id.sms_advert_user_button) {

            } else if (v.getId() == R.id.share_button) {

            }
        }
    }

    private class ViewHolder {
        ImageView productImageView;
        ImageButton favouriteButton;
        CustomTextView productNameTextView;
        CustomTextView productPriceTextView;
        CustomTextView productDetailsTextView;
        ImageButton callButton;
        ImageButton smsButton;
        ImageButton shareButton;
    }

}
