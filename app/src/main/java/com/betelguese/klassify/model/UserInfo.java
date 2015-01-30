package com.betelguese.klassify.model;

/**
 * Created by U on 1/30/2015.
 */
public class UserInfo {
    String fullname,email,password,mobile_number,profile_pic,location;

    public UserInfo(String fullname, String email, String password, String mobile_number, String profile_pic, String location) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile_number = mobile_number;
        this.profile_pic = profile_pic;
        this.location = location;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
