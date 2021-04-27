package com.phonybook.phony;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact.db";
    public static final String CONTACT_TABLE = "CONTACT_TABLE";
    public static final String C_ID = "ID";
    public static final String C_NAME = "C_NAME";
    public static final String C_PHONENUM = "C_PHONENUM";
    public static final String C_EMAIL = "C_EMAIL";
    public static final String C_IMAGE = "C_CONTACT";
    public static final String C_WORK_CONTACT = "C_WORK_CONTACT";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACT_TABLE + " ("
                + C_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + C_NAME + " TEXT,"
                + C_PHONENUM + " TEXT,"
                + C_EMAIL + " TEXT,"
                + C_IMAGE + " TEXT,"
                + C_WORK_CONTACT + " BOOL"
                + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
        onCreate(db);
    }

    public void addOne(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(C_NAME, contactModel.getName());
        cv.put(C_PHONENUM, contactModel.getPhonenum());
        cv.put(C_EMAIL, contactModel.getEmail());
        cv.put(C_IMAGE, contactModel.getImage());
        cv.put(C_WORK_CONTACT, contactModel.isWork());

        db.insert(CONTACT_TABLE, null, cv);
    }

    public void deleteOne(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = C_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(CONTACT_TABLE, whereClause, whereArgs);
    }

    public void updateOne(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(C_NAME, contactModel.getName());
        cv.put(C_PHONENUM, contactModel.getPhonenum());
        cv.put(C_EMAIL, contactModel.getEmail());
        cv.put(C_IMAGE, contactModel.getImage());
        cv.put(C_WORK_CONTACT, contactModel.isWork());

        String whereClause = C_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(contactModel.getId())};

        db.update(CONTACT_TABLE, cv, whereClause, whereArgs);
    }

    public List<ContactModel> selectAll() {
        List<ContactModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CONTACT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // move through the cursor (result set) and create new contacts object. Move them into the array list
            do {
                int contactID = cursor.getInt(0);
                String contactName = cursor.getString(1);
                String contactPhonenum = cursor.getString(2);
                String contactEmail = cursor.getString(3);
                String contactImage = cursor.getString(4);
                boolean contactWorkContact = cursor.getInt(5) == 1;

                ContactModel newContact = new ContactModel(contactID, contactName, contactPhonenum, contactEmail, contactImage, contactWorkContact);
                returnList.add(newContact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }


    public ContactModel findByPhoneNum(String number) {
        ContactModel newContact = null;
        String queryString = "SELECT * FROM " + CONTACT_TABLE + " WHERE " + C_PHONENUM + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, new String[]{number});

        if (cursor.moveToFirst()) {
            int contactID = cursor.getInt(0);
            String contactName = cursor.getString(1);
            String contactPhonenum = cursor.getString(2);
            String contactEmail = cursor.getString(3);
            String contactImage = cursor.getString(4);
            boolean contactWorkContact = cursor.getInt(5) == 1;

            newContact = new ContactModel(contactID, contactName, contactPhonenum, contactEmail, contactImage, contactWorkContact);
        }

        cursor.close();
        db.close();
        return newContact;
    }
}
