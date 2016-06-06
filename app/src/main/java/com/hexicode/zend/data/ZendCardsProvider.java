package com.hexicode.zend.data;

/**
 * Created by Brian on 6/6/2016.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ZendCardsProvider extends ContentProvider {

    ZendCardsDbHelper zendCardsDbHelper;
    @Override
    public boolean onCreate() {
        zendCardsDbHelper = new ZendCardsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        final SQLiteDatabase db = zendCardsDbHelper.getReadableDatabase();
        cursor = db.query(
                ZendCardsContract.MyCards.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = zendCardsDbHelper.getWritableDatabase();
        Uri returnUri;

        long _id = db.insert(ZendCardsContract.MyCards.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = ZendCardsContract.MyCards.buildContactUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
