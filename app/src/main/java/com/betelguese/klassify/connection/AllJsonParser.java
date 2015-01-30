package com.betelguese.klassify.connection;

import android.content.Context;
import android.widget.Toast;

import com.betelguese.klassify.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by U on 1/30/2015.
 */
public class AllJsonParser {

    public static String TAG_STATUS = "status";
    public static String TAG_DATA = "data";
    public static String FULL_NAME = "fullname";
    public static String EMAIL = "email";
    public static String PASSWORD = "password";
    public static String MOBILE_NUMBER = "mobile_number";
    public static String PROFILE_PIC = "profile_pic";
    public static String LOCATION = "location";

    public static UserInfo loginJsonParser(JSONObject jsonObject ){
        try {
            String successOrFail = jsonObject.getString(TAG_STATUS);
            if (successOrFail.equals("success")) {
                JSONObject data = jsonObject.getJSONObject(TAG_DATA);
                String fullname = data.getString(FULL_NAME);
                String email = data.getString(EMAIL);
                String password = data.getString(PASSWORD);
                String mobile_number = data.getString(MOBILE_NUMBER);
                String profile_pic;
                if(!data.isNull(PROFILE_PIC))
                    profile_pic = data.getString(PROFILE_PIC);
                else profile_pic = "";
                String location = data.getString(LOCATION);

                UserInfo userInfo = new UserInfo(fullname,email,password,mobile_number,profile_pic,location);

                return userInfo;


            } else {
                return null;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
