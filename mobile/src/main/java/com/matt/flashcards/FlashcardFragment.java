package com.matt.flashcards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

public class FlashcardFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_flashcard, container, false);

        TextView frontTextView = rootView.findViewById(R.id.flashcard_front_textview);
        frontTextView.setText(getArguments().getString("Front"));

        TextView backTextView = rootView.findViewById(R.id.flashcard_back_textview);
        backTextView.setText(getArguments().getString("Back"));

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Flips flashcard back to the front when focus is lost
        if (!isVisibleToUser && rootView != null && ((EasyFlipView) rootView).isBackSide()) {
            // Refreshing the fragment works best: https://stackoverflow.com/a/46447226
            getFragmentManager()
                    .beginTransaction()
                    .detach(FlashcardFragment.this)
                    .attach(FlashcardFragment.this)
                    .commit();
        }
    }
}
