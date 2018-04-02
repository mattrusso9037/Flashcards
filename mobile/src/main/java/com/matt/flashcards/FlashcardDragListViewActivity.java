package com.matt.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mylibrary.Deck;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import static com.matt.flashcards.FlashcardActivity.currentDeck;

public class FlashcardDragListViewActivity extends AppCompatActivity {

    protected boolean changesMade;
    protected boolean updateOnResume;
    private DragItemAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        if (updateOnResume && adapter != null) {
            adapter.notifyDataSetChanged();
            updateOnResume = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_drag_list_view);
        setTitle(currentDeck.getTitle());

        DragListView dragListView = findViewById(R.id.flashcard_drag_list_view);
        adapter = new FlashcardDragItemAdapter(this, currentDeck);
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
                currentDeck.currentCardIndex = toPosition;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_listview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_card_list_view:
//                updateOnResume = true;
                startActivity(new Intent(this, AddEditActivity.class)
                        .putExtra("EditMode", false));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        onNavigateUp();
    }
}
