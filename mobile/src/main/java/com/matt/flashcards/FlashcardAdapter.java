package com.matt.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FlashcardAdapter extends ArrayAdapter {

    private int lastPosition = -1;

    public FlashcardAdapter(Context context, ArrayList<Flashcard> flashcards) {
        super(context, 0, flashcards);
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

        // Event for the edit button
        listItemView.findViewById(R.id.flashcard_item_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Edit item " + position, Toast.LENGTH_SHORT).show();
            }
        });

        // Event for the delete button
        listItemView.findViewById(R.id.flashcard_item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Delete item " + position, Toast.LENGTH_SHORT).show();
            }
        });

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