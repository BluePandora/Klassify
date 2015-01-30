package com.betelguese.klassify.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.AppController;
import com.betelguese.klassify.connection.AlertDialogForAnything;
import com.betelguese.klassify.connection.AllJsonParser;
import com.betelguese.klassify.connection.ConnectionDetector;
import com.betelguese.klassify.model.UserInfo;
import com.widget.CustomButton;
import com.widget.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by tuman on 26/1/2015.
 */
public class LogInFragment extends Fragment implements View.OnClickListener,Response.Listener<String> ,Response.ErrorListener {

    ProgressDialog pDialog;
    ConnectionDetector cd ;

    CustomEditText emailEd, passwordEd;
    CustomButton loginB,forgetPassB,createAccountB;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        }

        init();
        addListener();

        return rootView;
    }

    private void init() {
        cd = new ConnectionDetector(getActivity());

        emailEd = (CustomEditText) rootView.findViewById(R.id.emailEd);
        passwordEd = (CustomEditText) rootView.findViewById(R.id.passwordEd);
        loginB = (CustomButton) rootView.findViewById(R.id.login_button);
        forgetPassB = (CustomButton) rootView.findViewById(R.id.forgot_button);
        createAccountB = (CustomButton) rootView.findViewById(R.id.create_account_button);

    }

    private void addListener() {
        loginB.setOnClickListener(this);
        forgetPassB.setOnClickListener(this);
        createAccountB.setOnClickListener(this);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                actionLoginButton();
                break;
            case R.id.forgot_button:
                actionForgetButton();
                break;
            case R.id.create_account_button:
                actionCreateAcountButton();
                break;
        }
    }

    private void actionLoginButton() {

        if (cd.isConnectingToInternet()) {
            if (showWarningDialog()) {
                volleyRequest();
            }
        }

    }

    private void volleyRequest() {
        String tag_string_req = "string_req";

        String url = getActivity().getString(R.string.login_url);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Progrssing...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, this , this ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", emailEd.getText().toString().trim());
                params.put("password", passwordEd.getText().toString().trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void actionForgetButton() {

    }

    private void actionCreateAcountButton() {

    }

    @Override
    public void onResponse(String response) {
        Log.d("data face", response.toString());
        try {
            UserInfo userInfo = AllJsonParser.loginJsonParser(new JSONObject(response));
            if(userInfo != null){
                pDialog.hide();
            }else{
                pDialog.hide();
                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Fail", "Wrong Email address or Password.", false);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            pDialog.hide();
            Log.e("LognFragment", "Parsing Error");
        }


        pDialog.hide();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        VolleyLog.d("Error respose", "Error: " + volleyError.getMessage());

        pDialog.hide();
    }


    public boolean showWarningDialog() {
        if (passwordEd.getText().toString().isEmpty()) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Password  can't be Empty", false);

        } else if (!validEmail(emailEd.getText().toString())) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "It is not an valid email address.", false);

        } else {
            return true;
        }
        return false;
    }


    private boolean matchPass(String passSt, String conPassSt) {
        // TODO Auto-generated method stub
        if (passSt.equals(conPassSt)) {
            return true;
        }
        return false;
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }



}
