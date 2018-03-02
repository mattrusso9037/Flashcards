package com.matt.flashcards;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.matt.flashcards.database.FlashCardContract;
import com.matt.flashcards.database.FlashCardDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDatabaseInfo();
    }


    private void displayDatabaseInfo() {
        FlashCardDbHelper mDbHelper = new FlashCardDbHelper(this);  //acces db
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); //create/open db and READ
        //sql query to read all rows from table
        Cursor cursor = db.rawQuery("SELECT * FROM " + FlashCardContract.FlashCardEntry.TABLE_NAME, null);
        try {
            //display the number of rows in the cursor
            TextView displayView = (TextView) findViewById(R.id.text_view_DbMsg);
            displayView.setText("Number of rows in Decks db table: " + cursor.getCount());
        } finally {
            cursor.close();

        }
    }



}