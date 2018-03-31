package com.matt.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import static com.matt.flashcards.FlashcardActivity.currentDeck;

public class FlashcardDragListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_drag_list_view);

        DragListView dragListView = findViewById(R.id.flashcard_drag_list_view);
        DragItemAdapter adapter = new FlashcardDragItemAdapter(currentDeck);
        dragListView.setAdapter(adapter, true);
        dragListView.setLayoutManager(new LinearLayoutManager(this));
        dragListView.setCanDragHorizontally(false);
        dragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                new DebugToast(FlashcardDragListViewActivity.this, "Start - position: " + position);
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {}

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    new DebugToast(FlashcardDragListViewActivity.this, "End - position: " + toPosition);
                }
            }
        });
    }
}
