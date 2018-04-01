package com.matt.flashcards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.woxthebox.draglistview.BoardView;

import static com.matt.flashcards.Settings.theDeckOfDecks;

public class BoardActivity extends AppCompatActivity {

    private boolean changesMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        BoardView boardView = findViewById(R.id.board_view);
        boardView.setSnapToColumnsWhenScrolling(true);
        boardView.setSnapToColumnWhenDragging(true);
        boardView.setSnapDragItemToTouch(true);
        boardView.setSnapToColumnInLandscape(false);
        boardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        boardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {}

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    changesMade = true;
                }
            }

            @Override
            public void onItemChangedPosition(int oldColumn, int oldRow, int newColumn, int newRow) {}

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {}

            @Override
            public void onFocusedColumnChanged(int oldColumn, int newColumn) {}
        });

        FlashcardDragItemAdapter adapter;
        View header;
        RecyclerView recyclerView;
        for (int i = 0; i < theDeckOfDecks.size(); i++) {
            adapter = new FlashcardDragItemAdapter(this, theDeckOfDecks.get(i));
            header = View.inflate(this, R.layout.column_header, null);
            ((TextView) header.findViewById(R.id.column_text)).setText(theDeckOfDecks.get(i).getTitle());
            boardView.addColumnList(adapter, header, false);
            recyclerView = boardView.getRecyclerView(i);
            recyclerView.setVerticalScrollBarEnabled(true);
            recyclerView.setScrollbarFadingEnabled(false);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(this,
                            ((LinearLayoutManager)
                                    recyclerView.getLayoutManager()).getOrientation()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (changesMade) {
            Settings.saveData(this);
        }
    }
}
