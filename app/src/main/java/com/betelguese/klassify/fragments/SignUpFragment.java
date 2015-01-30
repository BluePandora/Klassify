package com.betelguese.klassify.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
public class SignUpFragment extends Fragment implements  View.OnClickListener ,Response.Listener<String> ,Response.ErrorListener {

    ProgressDialog pDialog;
    ConnectionDetector cd;
    CustomEditText fullNameEd,emailEd,mobileNumberEd,confirmPassEd,passwordEd;
    CustomButton signUpB,fbSignupB,googleSignUpB;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        }

        init();
        addListener();



        return rootView;
    }

    private void init() {
        cd = new ConnectionDetector(getActivity());

        fullNameEd = (CustomEditText) rootView.findViewById(R.id.fullNameEd);
        emailEd  = (CustomEditText) rootView.findViewById(R.id.emailEd);
        mobileNumberEd = (CustomEditText) rootView.findViewById(R.id.mobileNumberEd);
        confirmPassEd = (CustomEditText) rootView.findViewById(R.id.confirmPassEd);
        passwordEd  = (CustomEditText) rootView.findViewById(R.id.passwordEd);
        signUpB  = (CustomButton) rootView.findViewById(R.id.sign_up_button);
        fbSignupB  = (CustomButton) rootView.findViewById(R.id.facebook_button);
        googleSignUpB  = (CustomButton) rootView.findViewById(R.id.google_plus_button);
    }

    private void addListener() {

        signUpB.setOnClickListener(this);
        fbSignupB.setOnClickListener(this);
        googleSignUpB.setOnClickListener(this);
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
            case R.id.sign_up_button:
                signUpButtonActivity();
                break;
            case R.id.facebook_button:
                fbButtonActivity();
                break;
            case R.id.google_plus_button:
                googleButtonActivity();
                break;
        }
    }

    private void signUpButtonActivity() {

        if (cd.isConnectingToInternet()) {
            if (showWarningDialog()) {
                volleyRequest();
            }
        }

    }

    private void volleyRequest() {
        String tag_string_req = "string_req";

        String url = getActivity().getString(R.string.signup_url);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Progrssing...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, this , this ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("fullname", fullNameEd.getText().toString().trim());
                params.put("email", emailEd.getText().toString().trim());
                params.put("password", passwordEd.getText().toString().trim());
                params.put("mobile_number", mobileNumberEd.getText().toString().trim());

                params.put("profile_pic", "");
                params.put("location", "");

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fbButtonActivity() {

    }

    private void googleButtonActivity() {

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        VolleyLog.d("Error respose", "Error: " + volleyError.getMessage());

        pDialog.hide();
    }

    @Override
    public void onResponse(String response) {
        try {
            String responseString = new JSONObject(response).getString("status");
            if (responseString.equals("success")) {

                String fullName = fullNameEd.getText().toString();
                String email = emailEd.getText().toString();
                String password = passwordEd.getText().toString();
                String mobileNumber = mobileNumberEd.getText().toString();
                String profilePic = "";
                String location = "";


                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                pDialog.hide();
                /*Intent intent = new Intent(this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            } else {
                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Fail", "Fail to sign up.", false);
                pDialog.hide();

            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        pDialog.hide();
    }



    public boolean showWarningDialog() {
        if (fullNameEd.getText().toString().isEmpty()) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Fist Name can't be Empty", false);

        } else if (emailEd.getText().toString().isEmpty()) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Email  can't be Empty", false);

        }  else if (mobileNumberEd.getText().toString().isEmpty()) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Mobile Number  can't be Empty", false);

        }else if (passwordEd.getText().toString().isEmpty()) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Password  can't be Empty", false);

        } else if (confirmPassEd.getText().toString().isEmpty()) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Confrim Password  can't be Empty", false);

        } else if (!matchPass(passwordEd.getText().toString(), confirmPassEd.getText().toString())) {
            AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Warning", "Password missmatch", false);

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
