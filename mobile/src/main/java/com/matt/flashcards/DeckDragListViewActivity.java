package com.matt.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import static com.matt.flashcards.FlashcardActivity.currentDeck;

public class DeckDragListViewActivity extends AppCompatActivity {

    private boolean changesMade;
    private boolean updateOnResume;
    private DragItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_drag_list_view);

        DragListView dragListView = findViewById(R.id.flashcard_drag_list_view);
        adapter = new DeckDragItemAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = dragListView.getRecyclerView();

        dragListView.setAdapter(adapter, true);
        dragListView.setLayoutManager(layoutManager);
        dragListView.setCanDragHorizontally(false);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation()));
        dragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {}

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {}

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    changesMade = true;
                }
            }
        });
    }

    // Save changes when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        if (changesMade) {
            Settings.saveData(this);
        }
    }
}