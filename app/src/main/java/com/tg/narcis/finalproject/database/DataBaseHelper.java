package com.tg.narcis.finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tg.narcis.finalproject.User;

public class DataBaseHelper extends SQLiteOpenHelper{

    /*
    * DEFINITION:
    * This class is the one which talks with the DB.
    */
    private final String TAG = "DataBaseHelper";

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Users.db";


    private static final String SQL_CREATE_Users =
            "CREATE TABLE " + DataBaseContract.Users.TABLE_NAME + " (" +
                    DataBaseContract.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DataBaseContract.Users.COLUMN_USER + " STRING UNIQUE," +
                    DataBaseContract.Users.COLUMN_PASS + " STRING)";

    private static final String SQL_DELETE_Users =
            "DROP TABLE IF EXISTS " + DataBaseContract.Users.TABLE_NAME;

    private static DataBaseHelper instance;
    private static SQLiteDatabase writable;
    private static SQLiteDatabase readable;

    //We will use this method instead the default constructor to get a reference.
    //With this we will use all the time the same reference.
    public static DataBaseHelper getInstance(Context c){
        if(instance == null){
            instance = new DataBaseHelper(c);
            if (writable == null) writable = instance.getWritableDatabase();
            if (readable == null) readable = instance.getReadableDatabase();
        }
        return instance;
    }

    //With this, all must use getInstance(Context) to use this class
    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //We execute here the SQL sentence to create the DB
        sqLiteDatabase.execSQL(SQL_CREATE_Users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //This method will be executed when the system detects that DATABASE_VERSION has been upgraded
        sqLiteDatabase.execSQL(SQL_DELETE_Users);
        onCreate(sqLiteDatabase);
    }

    public long createUser(String user, String pass) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.Users.COLUMN_USER, user);
        values.put(DataBaseContract.Users.COLUMN_PASS, pass);
        long newId = writable.insert(DataBaseContract.Users.TABLE_NAME,null,values);
        return newId;
    }

    public int updateUser(String user, String pass) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.Users.COLUMN_USER, user.charAt(0)+user);
        values.put(DataBaseContract.Users.COLUMN_PASS, pass.charAt(0)+pass);
        int rows_afected = readable.update(DataBaseContract.Users.TABLE_NAME,    //Table name
                values,                                                             //New value for columns
                DataBaseContract.Users.COLUMN_USER + " LIKE ? ",                 //Selection args
                new String[] {user});                                                  //Selection values

        return rows_afected;
    }

    public int deleteUser(String s) {
        int afected = readable.delete(DataBaseContract.Users.TABLE_NAME,         //Table name
                DataBaseContract.Users.COLUMN_USER + " LIKE ? ",                 //Selection args
                new String[] {s});                                                  //Selection values

        return afected;
    }

    public User queryUser(String s) {
        Cursor c;
        c = readable.query(DataBaseContract.Users.TABLE_NAME,    //Table name
                new String[] {DataBaseContract.Users.COLUMN_USER, DataBaseContract.Users.COLUMN_PASS},       //Columns we select
                DataBaseContract.Users.COLUMN_USER + " = ? ",    //Columns for the WHERE clause
                new String[] {s},                                   //Values for the WHERE clause
                null,                                               //Group By
                null,                                               //Having
                null);                                              //Sort

        User returnValue = new User();

        if (c.moveToFirst()) {
            do {
                //We go here if the cursor is not empty
                String user = c.getString(c.getColumnIndex(DataBaseContract.Users.COLUMN_USER));
                returnValue.setUsername(user);
                String pass = c.getString(c.getColumnIndex(DataBaseContract.Users.COLUMN_PASS));
                returnValue.setPassword(pass);
                Log.v("db", "user: " + user +" pass: " + pass);
            } while (c.moveToNext());
        }
        else
            returnValue = null;

        //Always close the cursor after you finished using it
        c.close();

        return returnValue;
    }

    @Override
    public synchronized void close() {
        super.close();
        //Always close the SQLiteDatabase
        writable.close();
        readable.close();
        Log.v(TAG,"close()");
    }
}
