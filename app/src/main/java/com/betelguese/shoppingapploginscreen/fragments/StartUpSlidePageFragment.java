package com.betelguese.shoppingapploginscreen.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.betelguese.shoppingapploginscreen.R;

/**
 * Created by tuman on 25/1/2015.
 */
public class StartUpSlidePageFragment extends Fragment {

    private View rootView;
    private ImageView shoppingImageView;
    private static final String POSITION = "position";
    private static final int PAGE_ONE_RES = R.drawable.shopping_1;
    private static final int PAGE_TWO_RES = R.drawable.shopping_2;
    private static final int PAGE_THREE_RES = R.drawable.shopping_3;

    public static Fragment newInstance(int position) {
        Fragment mContent = new StartUpSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        mContent.setArguments(bundle);
        return mContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_startup, container, false);
        }
        final int position = getArguments().getInt(POSITION);
        switch (position) {
            case 0:
                rootView = inflater.inflate(R.layout.fragment_startup_one, container, false);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.fragment_startup_two, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_startup_three, container, false);
                break;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
