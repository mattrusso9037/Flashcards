package com.matt.flashcards.database;

import android.provider.BaseColumns;

/**
 * Created by gregb on 3/1/2018.
 */

public final class FlashCardContract {

    private FlashCardContract() {}  //empty constructor to stop instantiating by oops

    public static abstract class FlashCardEntry implements BaseColumns {
        public final static String TABLE_NAME = "Decks";    //table name
        public final static String _ID = BaseColumns._ID; //unique ID for card
        public final static String COLUMN_CARD_DECK = "Deck Name"; //name of deck card belongs to
        public final static String COLUMN_CARD_QUESTION = "Question"; //question on the card
        public final static String COLUMN_CARD_ANSWER = "Answer";   //answer on the card

    }
}
