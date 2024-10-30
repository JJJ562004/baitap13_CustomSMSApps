package com.example.cp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sms_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SMS = "sms";
    private static final String COLUMN_ID = "id";
    private static final String PHONE = "address";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SMS_TABLE = "CREATE TABLE " + TABLE_SMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PHONE + " TEXT,"
                + COLUMN_BODY + " TEXT,"
                + COLUMN_DATE + " TEXT" + ")";
        db.execSQL(CREATE_SMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
        onCreate(db);
    }

    public void addSms(SMS sms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHONE, sms.getAddress());
        values.put(COLUMN_BODY, sms.getBody());
        values.put(COLUMN_DATE, sms.getDate());
        db.insert(TABLE_SMS, null, values);
        db.close();
    }

    public List<SMS> getAllSms() {
        List<SMS> smsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") SMS sms = new SMS(
                        cursor.getString(cursor.getColumnIndex(PHONE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_BODY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                );
                smsList.add(sms);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return smsList;
    }
}
