package com.matt.flashcards;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeckAdapter extends ArrayAdapter {

    public DeckAdapter(Context context, ArrayList<Deck> decks) {
        super(context, 0, decks);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.deck_item, parent, false);
        }

        final Deck currentDeck = (Deck) getItem(position);

        ((TextView) listItemView.findViewById(R.id.deck_title)).setText(currentDeck.getTitle());

        // Event for when a deck is clicked
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flashCardView = new Intent(getContext(), SP_FlashcardViewerActivity.class);
                flashCardView.putExtra("Index", position);
                getContext().startActivity(flashCardView);
            }
        });

        return listItemView;
    }
}
