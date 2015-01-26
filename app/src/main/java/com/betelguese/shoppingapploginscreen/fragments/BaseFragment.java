package com.betelguese.shoppingapploginscreen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.betelguese.shoppingapploginscreen.R;

/**
 * Created by tuman on 20/1/2015.
 */
public class BaseFragment extends Fragment {
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        }
        return rootView;
    }

}
