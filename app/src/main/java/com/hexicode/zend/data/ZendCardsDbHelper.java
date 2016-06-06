package com.hexicode.zend.data;

/**
 * Created by Brian on 6/6/2016.
 */

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Brian on 9/1/2015.
 */
public class ZendCardsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "zend.db";

    public ZendCardsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MYCARDS_TABLE = "CREATE TABLE " +
                ZendCardsContract.MyCards.TABLE_NAME + "(" +
                ZendCardsContract.MyCards._ID + " INTEGER PRIMARY KEY, " +
                ZendCardsContract.MyCards.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                ZendCardsContract.MyCards.COLUMN_LAST_NAME + " TEXT, " +
                ZendCardsContract.MyCards.COLUMN_COMPANY_ASSOCIATION + " TEXT, " +
                ZendCardsContract.MyCards.COLUMN_EMAIL_ADDRESS + " TEXT, " +
                ZendCardsContract.MyCards.COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                ZendCardsContract.MyCards.COLUMN_PHOTO_ADDRESS + " TEXT NOT NULL);";

        final String SQL_CREATE_CARDS_TABLE = "CREATE TABLE " +
                ZendCardsContract.Cards.TABLE_NAME + "(" +
                ZendCardsContract.Cards._ID + " INTEGER PRIMARY KEY, " +
                ZendCardsContract.Cards.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                ZendCardsContract.Cards.COLUMN_LAST_NAME + " TEXT, " +
                ZendCardsContract.Cards.COLUMN_COMPANY_ASSOCIATION + " TEXT, " +
                ZendCardsContract.Cards.COLUMN_EMAIL_ADDRESS + " TEXT, " +
                ZendCardsContract.Cards.COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                ZendCardsContract.Cards.COLUMN_PHOTO_ADDRESS + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MYCARDS_TABLE);
        db.execSQL(SQL_CREATE_CARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ZendCardsContract.MyCards.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZendCardsContract.Cards.TABLE_NAME);
        onCreate(db);
    }
}