package com.betelguese.klassify.appdata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ~Flash~ on 1/30/2015.
 */
public class SubCategory implements Parcelable {
    private String id, title;
    private ArrayList<String> fields;

    public SubCategory(String id, String title, ArrayList<String> fields) {
        this.id = id;
        this.title = title;
        this.fields = fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeStringList(fields);
    }
}
