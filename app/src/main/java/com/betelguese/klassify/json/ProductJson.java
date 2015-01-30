package com.betelguese.klassify.json;

import com.betelguese.klassify.appdata.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Md.Ashraful Islam Reg No. 2010331035 Computer Science and Engineering
 * Shahjalal University of Science and Technology,Sylhet
 */

public class ProductJson {
    private final String ID = "product_id";
    private final String TITLE = "title";
    private final String DESCRIPTION = "description";
    private final String PRICE = "price";
    private final String IMAGE = "image";
    private final String CATEGORY = "category";
    private final String EMAIL = "email";
    private final String CREATED_DATE = "created_date";

    private String link;
    private int pointer = -1;

    public ProductJson(String link) {
        this.link = link;
    }

    public ArrayList<Product> getProducts() {
        ArrayList<Product> news = new ArrayList<Product>();
        JSONParser parser = new JSONParser();
        JSONArray productList;
        try {
            parser = new JSONParser();
            productList = parser.getJSONArrayFromUrl(link);
            for (int i = 0; i < productList.length(); i++) {
                try {
                    JSONObject newsItem = productList.getJSONObject(i);
                    String id = getData(newsItem, ID);
                    String title = getData(newsItem, TITLE);
                    String description = getData(newsItem, DESCRIPTION);
                    String image = getData(newsItem, IMAGE);
                    String category = getData(newsItem, CATEGORY);
                    String email = getData(newsItem, EMAIL);
                    double price = Double.parseDouble(getData(newsItem, PRICE));
                    String createdDate = getData(newsItem, CREATED_DATE);
                    news.add(new Product(id, title, description, price, image, category, email, createdDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }


    private String getData(JSONObject jdata, String key) {
        String data = "";
        try {
            data = jdata.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getPointer() {
        return pointer;
    }
}