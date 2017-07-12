package com.tg.narcis.finalproject.database;

import android.provider.BaseColumns;

public final class DataBaseContract {
    //We prevent that someone extends this class by making it final

    /*
    * DEFINITION:
    * A formal declaration of how the database is organized.
    * Definition by Google in: https://developer.android.com/training/basics/data-storage/databases.html
    */

    // We prevent that someone instantiate this class making the constructor private
    private DataBaseContract() {
    }

    //We create as much public static classes as tables we have in our database
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        //public static final String COLUMN_ID = "id"; <- Actually we don't need a dedicated COLUMN_ID
        //because this class implements BaseColumns which has a _ID constant for that
        public static final String COLUMN_USER = "username";
        public static final String COLUMN_PASS = "password";
        public static final String COLUMN_SCORE= "score";
    }
}