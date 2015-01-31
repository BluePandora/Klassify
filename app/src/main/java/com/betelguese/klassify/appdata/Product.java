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
    String  phone,email, createdDate;
    private double price;
    boolean isFavourite;
    String category;
    String subCategory;
    String field;


    public Product() {

    }

    public Product(String productId, String title, String description, ArrayList<String> images, String phone, String email, String createdDate, double price, boolean isFavourite, String category, String subCategory, String field) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.images = images;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
        this.price = price;
        this.isFavourite = isFavourite;
        this.category = category;
        this.subCategory = subCategory;
        this.field = field;
    }

    public Product(String productId, String title, String description, ArrayList<String> images, String phone, String email, String createdDate, double price, boolean isFavourite) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.images = images;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
        this.price = price;
        this.isFavourite = isFavourite;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
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
