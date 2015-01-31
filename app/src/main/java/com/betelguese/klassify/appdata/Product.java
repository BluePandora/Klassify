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
    private String phone;
    private boolean isFavorite;
    private String category;
    private String subCategory;
    private String field;

    public Product(String productId, String title, String description, ArrayList<String> images, String phone, String email, String createdDate, double price, boolean isFavorite) {
        if (images != null && images.size() != 0)
            this.images = images;
        else {
            this.images = new ArrayList<>();
            this.images.add(noImage);
        }
        this.title = title;
        this.description = description;
        this.productId = productId;
        this.email = email;
        this.createdDate = createdDate;
        this.price = price;
        this.phone = phone;
        this.isFavorite = isFavorite;
    }

    public Product(String productId, String title, String description, ArrayList<String> images, String phone, String email, String createdDate, double price, boolean isFavorite, String category, String subcategory, String field) {
        if (images != null && images.size() != 0)
            this.images = images;
        else {
            this.images = new ArrayList<>();
            this.images.add(noImage);
        }
        this.title = title;
        this.description = description;
        this.productId = productId;
        this.email = email;
        this.createdDate = createdDate;
        this.price = price;
        this.phone = phone;
        this.isFavorite = isFavorite;
        this.category = category;
        this.subCategory = subcategory;
        this.field = field;
    }

    public Product(String productId, String title, String description, ArrayList<String> images, String phone, String email, String createdDate, double price) {
        if (images != null && images.size() != 0)
            this.images = images;
        else {
            this.images = new ArrayList<>();
            this.images.add(noImage);
        }
        this.title = title;
        this.description = description;
        this.productId = productId;
        this.email = email;
        this.createdDate = createdDate;
        this.price = price;
        this.phone = phone;
    }

    public Product(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        if (images == null) setImages(null);
        in.readStringList(images);
        this.phone = in.readString();
        this.productId = in.readString();
        this.email = in.readString();
        this.createdDate = in.readString();
        this.price = in.readDouble();
        this.isFavorite = in.readByte() != 0;
        this.category = in.readString();
        this.subCategory = in.readString();
        this.field = in.readString();
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(images);
        dest.writeString(phone);
        dest.writeString(productId);
        dest.writeString(email);
        dest.writeString(createdDate);
        dest.writeDouble(price);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(category);
        dest.writeString(subCategory);
        dest.writeString(field);
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        if (images != null && images.size() != 0) {
            return images.get(0);
        } else return noImage;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
