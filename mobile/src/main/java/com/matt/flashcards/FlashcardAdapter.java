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

        // Event for the view button
        listItemView.findViewById(R.id.flashcard_item_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SP_FlashcardViewerActivity.currentDeck.currentCardIndex = position;
                ((SP_FlashcardViewerActivity.FlashcardListActivity) getContext()).onNavigateUp();
            }
        });

        // Event for the edit button
        listItemView.findViewById(R.id.flashcard_item_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), AddEditActivity.class)
                        .putExtra("EditMode", true)
                        .putExtra("DeckIndex", Deck.currentDeckIndex)
                        .putExtra("CardIndex", position));
            }
        });

        // Event for the delete button
        listItemView.findViewById(R.id.flashcard_item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure you want to delete this flashcard?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deck.remove(position);
                                if (SP_FlashcardViewerActivity.currentDeck.currentCardIndex
                                        == SP_FlashcardViewerActivity.currentDeck.size()) {
                                    SP_FlashcardViewerActivity.currentDeck.currentCardIndex--;
                                }
                                notifyDataSetChanged();
                                Settings.saveData(getContext());
                            }
                        }).setNegativeButton("Cancel", null)
                        .create().show();
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
