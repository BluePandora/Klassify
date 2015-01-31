package com.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.betelguese.klassify.appdata.Product;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tuman on 30/1/2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {


    String[] tableName = {"favourite", "images"};

    String[][] tableItem = new String[][]{{"productId", "title", "description", "email", "createdDate", "price", "phone"}, {"productId", "image"}};
    String[][] tableItemType = new String[][]{{"String  PRIMARY KEY", "String", "String", "String", "String", "String", "String"}, {"String", "String"}};


    String PRODUCT_ID = "productId";
    String TITLE = "title";
    String DESCRIPTION = "description";
    String EMAIL = "email";
    String CREATED_DATE = "createdDate";
    String PRICE = "price";
    String PHONE = "phone";
    String IMAGE = "image";

    Context context;

    public DatabaseOpenHelper(Context context) {
        super(context, "Klassify.db", null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, tableName, tableItem, tableItemType);
    }

    public void createTable(SQLiteDatabase db, String[] tableName, String[][] tableItem, String[][] tableItemType) {
        String[] keyWithType = new String[tableName.length];
        for (int k = 0; k < tableName.length; k++) {
            for (int l = 0; l < tableItem[k].length; l++) {
                if (l != 0) {
                    keyWithType[k] = keyWithType[k] + ", " + tableItem[k][l] + " " + tableItemType[k][l];
                } else {
                    keyWithType[k] = tableItem[k][l] + " " + tableItemType[k][l];
                }
            }
        }

        for (int i = 0; i < tableName.length; i++) {
            String createTableQuery = "CREATE TABLE " + tableName[i] + " (" + keyWithType[i] + ")";
            db.execSQL(createTableQuery);
        }
    }

    public void dropTable(SQLiteDatabase db, String[] tableName) {
        for (int i = 0; i < tableName.length; i++) {
            String dropTableQuery = "DROP TABLE IF EXISTS " + tableName[i];
            db.execSQL(dropTableQuery);
        }
    }

    public void insertTable(String tableName, String key1[], String value1[]) {
        String key = getArrayToSingleString(key1, 2);
        String value = getArrayToSingleString(value1, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        String insertTableQuery = "INSERT OR REPLACE INTO " + tableName + "(" + key + ") VALUES (" + value + ")";
        db.execSQL(insertTableQuery);
        db.close();
    }


    public void updatetTable(String tableName, String key1[], String value1[], String whereKeyValue) {

        String updateStringCode = getUpdateStringCode(key1, value1);

        SQLiteDatabase db = this.getWritableDatabase();

        String updatequery = "UPDATE " + tableName + " SET " + updateStringCode + " " + whereKeyValue;
        //String insertTableQuery = "INSERT OR REPLACE INTO "+tableName+"("+key+") VALUES "+value;
        db.execSQL(updatequery);

        db.close();
    }


    public ArrayList<HashMap<String, String>> getAllfromTable(String tableName, String selectKeyArray[], String whereKeyValue, String distinct, String ordering) {

        String selectKey = getArrayToSingleString(selectKeyArray, 2);
        ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + distinct + " " + selectKey + " FROM " + tableName + " " + whereKeyValue + " " + ordering + "";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < selectKeyArray.length; i++) {
                    map.put(selectKeyArray[i], cursor.getString(i));
                }
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordList;
    }


    public void deleteAndClearTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String resultTableQuery = "DELETE From " + tableName;
        db.execSQL(resultTableQuery);
        db.close();

    }

    public void deleteFromTable(String tableName, String whereCon) {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteQuery = "DELETE FROM " + tableName + " " + whereCon + "";
        db.execSQL(deleteQuery);
        db.close();

    }


    public int existInTable(String tableName, String KeyValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE " + KeyValue + "";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            db.close();
            return 1;
        }
        db.close();
        return 0;

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version,
                          int current_version) {
        String resultTableQuery = "DROP TABLE IF EXISTS resultTable";
        db.execSQL(resultTableQuery);

        String questionSetQuery = "DROP TABLE IF EXISTS questionSet";
        db.execSQL(questionSetQuery);
        onCreate(db);
    }

    public int getCountRow(String tableName, String keyValue, String whereCon) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + keyValue + " FROM " + tableName + " " + whereCon + "";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        db.close();
        return 0;

    }

    public String getArrayToSingleString(String[] key1, int withCottetion) {
        // TODO Auto-generated method stub
        String key = "";
        if (withCottetion == 1) {
            for (int i = 0; i < key1.length; i++) {
                key1[i] = key1[i].replaceAll("'", "''");
                if (i != 0) {
                    key = key + ", '" + key1[i] + "'";
                } else {
                    key = "'" + key1[i] + "'";
                }
            }
        } else if (withCottetion == 2) {
            for (int i = 0; i < key1.length; i++) {
                if (i != 0) {
                    key = key + ", " + key1[i];
                } else {
                    key = key1[i];
                }
            }
        }
        return key;
    }

    private String getUpdateStringCode(String[] key1, String[] value1) {
        // TODO Auto-generated method stub
        String temp = "";
        for (int i = 0; i < key1.length; i++) {
            temp = temp + key1[i] + " = " + "'" + value1[i] + "'";
            if (i != key1.length - 1) {
                temp = temp + ", ";
            }
        }
        return temp;
    }

    /**
     * JUST new*
     */

    public void insertFavTable(Product productItem) {

        String value[] = new String[7];
        value[0] = productItem.getProductId();
        value[1] = productItem.getTitle();
        value[2] = productItem.getDescription();
        value[3] = productItem.getEmail();
        value[4] = productItem.getCreatedDate();
        value[5] = String.valueOf(productItem.getPrice());
        value[6] = productItem.getPhone();

        insertTable(tableName[0], tableItem[0], value);

        ArrayList<String> images = productItem.getImages();

        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                String valueImage[] = new String[2];
                valueImage[0] = productItem.getProductId();
                valueImage[1] = images.get(i);

                insertTable(tableName[1], tableItem[1], valueImage);
            }
        }
    }

    public void deleteFromFavTable(Product productItem) {

        deleteFromTable(tableName[0], " Where productId = " + productItem.getProductId());
        deleteFromTable(tableName[1], " Where productId = " + productItem.getProductId());
    }

    public ArrayList<Product> getAllfromFavTable() {
        ArrayList<Product> productList = new ArrayList<Product>();
        ArrayList<HashMap<String, String>> wordList = getAllfromTable(tableName[0], tableItem[0], "", "", "order by productId DESC");
        if (wordList != null) {
            for (int i = 0; i < wordList.size(); i++) {
                String productId = wordList.get(i).get(PRODUCT_ID);
                String title = wordList.get(i).get(TITLE);
                String description = wordList.get(i).get(DESCRIPTION);
                String email = wordList.get(i).get(EMAIL);
                String createdDate = wordList.get(i).get(CREATED_DATE);
                String phone = wordList.get(i).get(PHONE);
                double price = Double.parseDouble(wordList.get(i).get(PRICE));


                ArrayList<String> images = new ArrayList<String>();
                ArrayList<HashMap<String, String>> imagesList = getAllfromTable(tableName[1], tableItem[1], " Where productId = " + productId, "", "");
                if (imagesList != null) {
                    for (int j = 0; j < imagesList.size(); j++) {
                        images.add(imagesList.get(j).get(IMAGE));
                    }
                }

                Product productItem = new Product(productId, title, description, images, phone, email, createdDate, price, true);
                productList.add(productItem);
            }
        }
        Log.e("Ashraful", "size" + productList.size());
        return productList;
    }

    public boolean existInFavTable(String productId) {
        int checkInt = existInTable(tableName[0], " productId =" + productId);

        if (checkInt == 1) return true;
        else return false;

    }
}
