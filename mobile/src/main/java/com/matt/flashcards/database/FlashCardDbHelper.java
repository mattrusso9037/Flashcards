package com.matt.flashcards.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.matt.flashcards.database.FlashCardContract.FlashCardEntry;

/**
 * Created by gregb on 3/2/2018.
 */

public class FlashCardDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "flashcards.db"; //name of the derpabase
    private static final int DATABASE_VERSION = 1;  //db version number

    public FlashCardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table "Decks"
        String SQL_CREATE_DECKS_TABLE = "CREATE TABLE " + FlashCardEntry.TABLE_NAME + "("
                + FlashCardEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT"
                + FlashCardEntry.COLUMN_CARD_DECK + "TEXT NOT NULL"
                + FlashCardEntry.COLUMN_CARD_QUESTION + "TEXT NOT NULL"
                + FlashCardEntry.COLUMN_CARD_ANSWER + "TEXT NOT NULL";

        db.execSQL(SQL_CREATE_DECKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}
