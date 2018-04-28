package com.matt.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

public class FlashcardAdapter extends ArrayAdapter {

    private int lastPosition = -1;
    private Deck deck;

    public FlashcardAdapter(Context context, Deck flashcards) {
        super(context, 0, flashcards);
        this.deck = flashcards;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.flashcard_item, parent, false);
        }

        final Flashcard currentFlashcard = (Flashcard) getItem(position);

        ((TextView) listItemView.findViewById(R.id.flashcard_item_text)).setText(currentFlashcard.getSideA());

        View flashCardItem = listItemView.findViewById(R.id.flashcard_item_actions);
        flashCardItem.setVisibility(position == lastPosition ? View.VISIBLE : View.GONE);
        flashCardItem.setTag(Integer.toString(position));

        // Event for clicking on a flashcard item
        listItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Logic to make sure only one item will show buttons at a time
                if (lastPosition > -1) {
                    if (lastPosition == position) {
                        View v = parent.findViewWithTag(Integer.toString(position));
                        if (v != null) {
                            v.setVisibility(View.GONE);
                        }
                        lastPosition = -1;
                        return;
                    } else {
                        View v = parent.findViewWithTag(Integer.toString(lastPosition));
                        if (v != null) {
                            v.setVisibility(View.GONE);
                        }
                    }
                }
                parent.findViewWithTag(Integer.toString(position)).setVisibility(View.VISIBLE);
                lastPosition = position;
            }
        });

        return listItemView;
    }
}
