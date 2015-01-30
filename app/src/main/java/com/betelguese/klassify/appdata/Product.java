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
    private String productId, title, description;
    private ArrayList<String> images;
    private String email, createdDate;
    private double price;
    private final String noImage = "http://sustcse10.net/ashraful/emarket/product.jpg";


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
        if (images != null && images.size() != 0)
            this.images = images;
        else {
            this.images = new ArrayList<>();
            this.images.add(noImage);
        }
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

    public String getImage() {
        if (images != null && images.size() != 0) {
            return images.get(0);
        } else return noImage;
    }
}
