package com.betelguese.klassify.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.betelguese.klassify.R;
import com.betelguese.klassify.fragments.LogInFragment;
public class LogInActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LogInFragment())
                    .commit();
        }
    }
}
