package com.betelguese.klassify.appdata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ashraful on 1/27/2015.
 * Md.Ashraful Islam
 * Reg No. 2010331035
 * Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */
public class Product implements Parcelable {
    private String productId,title, description;
    ArrayList<String> images;
    String  email, createdDate;
    private double price;

    public Product() {

    }

    public Product(String productId, String title, String description, ArrayList<String> images, String email, String createdDate, double price) {
        this.title = title;
        this.description = description;
        this.images = images;
        this.productId = productId;
        this.email = email;
        this.createdDate = createdDate;
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public ArrayList<String> getImages() {
        return images;
    }

    public double getPrice() {
        return price;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }



    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
