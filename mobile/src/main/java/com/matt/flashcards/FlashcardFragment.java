package com.matt.flashcards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylibrary.Flashcard;

@SuppressLint("ValidFragment")
public class FlashcardFragment extends Fragment {

    private Flashcard flashcard;
    private boolean isFront = true;
    private TextView flashcardTextView;

    @SuppressLint("ValidFragment")
    public FlashcardFragment(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flashcard, container, false);

        this.flashcardTextView = rootView.findViewById(R.id.flashcard_textview);
        flashcardTextView.setText(flashcard.getSideA());
        flashcardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFront = !isFront;
                flashcardTextView.setText(isFront ? flashcard.getSideA() : flashcard.getSideB());
            }
        });

        return rootView;
    }

    // Flips flashcard back to Side A when focus is lost
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isFront && !isVisibleToUser && flashcardTextView != null) {
            flashcardTextView.setText(flashcard.getSideA());
            isFront = true;
        }
    }
}
