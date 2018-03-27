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
import com.wajahatkarim3.easyflipview.EasyFlipView;

@SuppressLint("ValidFragment")
public class FlashcardFragment extends Fragment {

    private Flashcard flashcard;
    private View rootView;

    @SuppressLint("ValidFragment")
    public FlashcardFragment(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_flashcard, container, false);

        TextView frontTextView = rootView.findViewById(R.id.flashcard_front_textview);
        frontTextView.setText(flashcard.getSideA());

        TextView backTextView = rootView.findViewById(R.id.flashcard_back_textview);
        backTextView.setText(flashcard.getSideB());

        View.OnClickListener flip = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EasyFlipView) rootView).flipTheView();
            }
        };
        frontTextView.setOnClickListener(flip);
        backTextView.setOnClickListener(flip);

        return rootView;
    }

    // Flips flashcard back to the front when focus is lost
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser && flashcard != null && rootView != null && ((EasyFlipView) rootView).isBackSide()) {
            ((EasyFlipView) rootView).setFlipDuration(0);
            ((EasyFlipView) rootView).flipTheView();
            ((EasyFlipView) rootView).setFlipDuration(EasyFlipView.DEFAULT_FLIP_DURATION);
        }
    }
}
