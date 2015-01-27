package com.betelguese.shoppingapploginscreen.appdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashraful on 1/27/2015.
 * Md.Ashraful Islam
 * Reg No. 2010331035
 * Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */
public class Product implements Parcelable {
    private String title, description, image, productId, category, email, createdDate;
    private double price;

    public Product(String productId, String title, String description, double price,String image, String category, String email, String createdDate) {
        this.title = title;
        this.description = description;
        this.productId = productId;
        this.category = category;
        this.price = price;
        this.email = email;
        this.createdDate = createdDate;
        this.image = image;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(createdDate);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(productId);
        dest.writeDouble(price);
    }

    public void setCategory(String category) {
        this.category = category;
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

    public void setImage(String image) {
        this.image = image;
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

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
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

    public String getImage() {
        return image;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }
}
