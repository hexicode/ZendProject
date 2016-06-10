package com.hexicode.zend.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Brian on 9/1/2015.
 */
public final class ZendCardsContract {

    public static final String CONTENT_AUTHORITY = "com.hexicode.zend";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final String PATH_MYCARDS = "my_cards";
    public static final String PATH_CARDS = "cards";

    public ZendCardsContract(){}

    public static abstract class MyCards implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYCARDS).build();
        public static final String TABLE_NAME = "myCards";
        public static final String COLUMN_NAME_ENTRY_ID = "myID";
        public static final String COLUMN_COMPANY_ASSOCIATION = "companyassociation";
        public static final String COLUMN_FIRST_NAME = "firstname";
        public static final String COLUMN_LAST_NAME = "lastname";
        public static final String COLUMN_PHONE_NUMBER = "phonenumber";
        public static final String COLUMN_EMAIL_ADDRESS = "email";

        //Actual photo will be saved on a file and retrieved by this column
        //file location string
        public static final String COLUMN_PHOTO_ADDRESS = "photolocation";

        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class Cards implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARDS).build();
        public static final String TABLE_NAME = "cards";
        public static final String COLUMN_NAME_ENTRY_ID = "cardid";
        public static final String COLUMN_COMPANY_ASSOCIATION = "companyassociation";
        public static final String COLUMN_FIRST_NAME = "firstname";
        public static final String COLUMN_LAST_NAME = "lastname";
        public static final String COLUMN_PHONE_NUMBER = "phonenumber";
        public static final String COLUMN_EMAIL_ADDRESS = "email";

        //Actual photo will be saved on a file and retrieved by this column
        //file location string
        public static final String COLUMN_PHOTO_ADDRESS = "photolocation";
    }
}