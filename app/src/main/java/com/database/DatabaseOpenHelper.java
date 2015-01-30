package com.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tuman on 30/1/2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    String[] tableName = {"favourite","images"};
    String[][] tableItem;
    String[][] tableItemType;
    Context context;

    public DatabaseOpenHelper(Context context) {
        super(context, "Klassify.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        tableItem = new String[2][];
        tableItemType = new String[tableName.length][];
        String tempFavTableItem[] = { "productId","title","description","images","email","createdDate","price" };
        tableItem[0] =  tempFavTableItem;
        String tempFavTableItemType[] = {"String","String","String","String","String","String","String"};
        tableItemType[0] = tempFavTableItemType;

        String tempImageItem[] = { "image_id","imageUrl" };
        tableItem[1] =  tempImageItem;
        String tempImageItemType[] = {"String","String"};
        tableItemType[1] = tempImageItemType;

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
        //String insertTableQuery = "INSERT OR REPLACE INTO "+tableName+"("+key+") VALUES "+value;
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
}
