package com.tg.narcis.finalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tg.narcis.finalproject.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper{

    /*
    * DEFINITION:
    * This class is the one which talks with the DB.
    */
    private final String TAG = "DataBaseHelper";

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Users.db";


    private static final String SQL_CREATE_Users =
            "CREATE TABLE " + DataBaseContract.Users.TABLE_NAME + " (" +
                    DataBaseContract.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DataBaseContract.Users.COLUMN_USER + " STRING UNIQUE," +
                    DataBaseContract.Users.COLUMN_PASS + " STRING," +
                    DataBaseContract.Users.COLUMN_SCORE + " STRING NOT NULL DEFAULT '-1')";

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

    public long createUser(String user, String pass, String score) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.Users.COLUMN_USER, user);
        values.put(DataBaseContract.Users.COLUMN_PASS, pass);
        values.put(DataBaseContract.Users.COLUMN_SCORE, score);
        long newId = writable.insert(DataBaseContract.Users.TABLE_NAME,null,values);
        return newId;
    }

    public int updateUser(String user, String pass, String score) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.Users.COLUMN_USER, user);
        values.put(DataBaseContract.Users.COLUMN_PASS, pass);
        values.put(DataBaseContract.Users.COLUMN_SCORE, score);
        int rows_afected = writable.update(DataBaseContract.Users.TABLE_NAME,    //Table name
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
                new String[] {DataBaseContract.Users.COLUMN_USER, DataBaseContract.Users.COLUMN_PASS, DataBaseContract.Users.COLUMN_SCORE},       //Columns we select
                DataBaseContract.Users.COLUMN_USER + " = ? ",    //Columns for the WHERE clause
                new String[] {s},                                   //Values for the WHERE clause
                null,                                               //Group By
                null,                                               //Having
                null);                                              //Sort

        User returnValue = new User(null,null,null);

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

    public ArrayList<User> queryAllUsers() {
        Cursor c;
        c = readable.query(DataBaseContract.Users.TABLE_NAME,    //Table name
                new String[] {DataBaseContract.Users.COLUMN_USER, DataBaseContract.Users.COLUMN_PASS, DataBaseContract.Users.COLUMN_SCORE},       //Columns we select
                null,    //Columns for the WHERE clause
                null,                                   //Values for the WHERE clause
                null,                                               //Group By
                null,                                               //Having
                DataBaseContract.Users.COLUMN_SCORE + " ASC");                                              //Sort

        ArrayList<User> userList = new ArrayList<>();

        if (c.moveToFirst()) {
            String user;
            do {
                User returnValue = new User(null,null,null);
                //We go here if the cursor is not empty
                user = c.getString(c.getColumnIndex(DataBaseContract.Users.COLUMN_USER));
                returnValue.setUsername(user);
                String pass = c.getString(c.getColumnIndex(DataBaseContract.Users.COLUMN_PASS));
                returnValue.setPassword(pass);
                String score = c.getString(c.getColumnIndex(DataBaseContract.Users.COLUMN_SCORE));
                returnValue.setScore(score);
                if(!score.equals("-1")) {
                    userList.add(returnValue);
                    Log.v("queryList", User.printUser(returnValue));
                }
            } while (c.moveToNext());
        }

        //Always close the cursor after you finished using it
        c.close();

        for(User user : userList) {
            Log.v("finalList", User.printUser(user));
        }
        return userList;
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
