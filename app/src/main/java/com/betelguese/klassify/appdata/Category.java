package com.betelguese.klassify.appdata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ~Flash~ on 1/30/2015.
 */
public class Category implements Parcelable {
    private String id, title;
    private ArrayList<SubCategory> subCategories;

    public Category(String id, String title, ArrayList<SubCategory> subCategories) {
        this.id = id;
        this.title = title;
        this.subCategories = subCategories;
    }

    public String getTitle() {
        return title;
    }


    public String getId() {
        return id;
    }


    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeTypedList(subCategories);
    }
}
