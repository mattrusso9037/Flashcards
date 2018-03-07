package com.matt.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        // Event for clicking on a flashcard item
        final View finalListItemView = listItemView;
        listItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Logic to make sure only one item will show buttons at a time
                if (lastPosition > -1) {
                    if (lastPosition == position) {
                        finalListItemView.findViewById(R.id.flashcard_item_actions).setVisibility(View.GONE);
                        lastPosition = -1;
                    } else {
                        // Hide all visible views
                        for (View item : getViewsByTag(parent, "flashcard_item_actions")) {
                            if (item.getVisibility() == View.VISIBLE) {
                                item.setVisibility(View.GONE);
                                break;
                            }
                        }
                        finalListItemView.findViewById(R.id.flashcard_item_actions).setVisibility(View.VISIBLE);
                        lastPosition = position;
                    }
                } else {
                    finalListItemView.findViewById(R.id.flashcard_item_actions).setVisibility(View.VISIBLE);
                    lastPosition = position;
                }
            }
        });

        return listItemView;
    }

    /**
     * https://stackoverflow.com/questions/8817377/android-how-to-find-multiple-views-with-common-attribute#8831593
     */
    private static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }
        return views;
    }
}
