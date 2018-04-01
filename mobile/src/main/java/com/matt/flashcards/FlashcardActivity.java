package com.matt.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Deck;
import com.example.mylibrary.Flashcard;

public class FlashcardActivity extends AppCompatActivity {

    private boolean isFullscreen = false;
    private boolean shuffleMode = false;
    private boolean favoriteMode = false;
    private boolean updateOnResume = false;
    private boolean changesMade;
    private ViewPager viewPager;
    private FlashcardFragmentPageAdapter pageAdapter;
    protected static Deck currentDeck;

    @Override
    protected void onResume() {
        super.onResume();

        // Update the viewpager on resume
        if (updateOnResume && pageAdapter != null) {
            pageAdapter.notifyDataSetChanged();
            updateOnResume = false;

            // Add the edit and delete menu items
            if (currentDeck.size() == 1) {
                invalidateOptionsMenu();
            }
        }

        if (viewPager != null) {
            if (favoriteMode) {
                // Changes the viewpager to the first flashcard
                viewPager.setCurrentItem(0);
            } else {
                // Changes the viewpager to the current flashcard
                viewPager.setCurrentItem(currentDeck.currentCardIndex, false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        Settings.loadData(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shuffleMode = extras.getBoolean("shuffleMode", false);
            favoriteMode = extras.getBoolean("favoriteMode", false);
        }

        if (shuffleMode) {
            currentDeck = Settings.shuffledDeck;
            setTitle(R.string.shuffle_mode);
            findViewById(R.id.flashcard_tip).setVisibility(View.INVISIBLE);
        } else if (favoriteMode) {
            currentDeck = Settings.favoritesDeck;
            setTitle(getString(R.string.favorites));
            findViewById(R.id.flashcard_tip).setVisibility(View.INVISIBLE);
        } else {
            currentDeck = Settings.theDeckOfDecks.get(Deck.currentDeckIndex);
            setTitle(currentDeck.getTitle());
            findViewById(R.id.flashcard_tip).setVisibility(View.VISIBLE);
        }

        // Get the viewpager
        viewPager = findViewById(R.id.viewpager);

        // Updates the Decks current card index whenever the page is changed
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentDeck.currentCardIndex = position;
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Set the adapter for the viewpager
        pageAdapter = new FlashcardFragmentPageAdapter(getSupportFragmentManager(), currentDeck);
        viewPager.setAdapter(pageAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (changesMade) {
            Settings.saveData(this);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem listViewItem = menu.findItem(R.id.action_list_view);
        MenuItem newItem = menu.findItem(R.id.action_new_card);
        MenuItem editItem = menu.findItem(R.id.action_edit_card);
        MenuItem deleteItem = menu.findItem(R.id.action_delete_card);
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        if (shuffleMode) {
            listViewItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(false);
            deleteItem.setVisible(false);
            if (currentDeck.getCurrentCard().isFavorite()) {
                favoriteItem.setIcon(R.drawable.ic_star_white_48dp);
            }
        } else if (favoriteMode) {
            listViewItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(false);
            deleteItem.setVisible(false);
            if (currentDeck.isEmpty()) {
                favoriteItem.setIcon(R.drawable.ic_star_border_white_48dp);
            } else {
                favoriteItem.setIcon(R.drawable.ic_star_white_48dp);
            }
        } else if (currentDeck.isEmpty()) {
            newItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            editItem.setVisible(false);
            deleteItem.setVisible(false);
        } else {
            newItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            editItem.setVisible(true);
            deleteItem.setVisible(true);
            if (currentDeck.getCurrentCard().isFavorite()) {
                favoriteItem.setIcon(R.drawable.ic_star_white_48dp);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // This adds menu items to the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Events for the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fullscreen:
                toggleFullscreen();
                getSupportActionBar().hide();
                return true;
            case R.id.action_new_card:
                newFlashCard();
                return true;
            case R.id.action_edit_card:
                editFlashCard();
                return true;
            case R.id.action_delete_card:
                deleteFlashCard();
                return true;
            case R.id.action_list_view:
                if (currentDeck.isEmpty()) {
                    Toast.makeText(this, R.string.empty_deck, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, FlashcardDragListViewActivity.class));
                }
                return true;
            case R.id.action_favorite:
                if (currentDeck.isEmpty()) {
                    return true;
                }
                Flashcard currentCard = currentDeck.getCurrentCard();
                boolean isFavorite = currentCard.isFavorite();
                currentCard.setFavorite(!isFavorite);
                if (isFavorite) {
                    Settings.favoritesDeck.remove(currentCard);
                } else {
                    Settings.favoritesDeck.add(currentCard);
                }
                if (favoriteMode) {
                    pageAdapter.notifyDataSetChanged();
                }
                invalidateOptionsMenu();
                changesMade = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newFlashCard() {
        updateOnResume = true;
        startActivity(new Intent(this, AddEditActivity.class)
                .putExtra("EditMode", false));
    }

    private void editFlashCard() {
        updateOnResume = true;
        startActivity(new Intent(this, AddEditActivity.class)
                .putExtra("EditMode", true)
                .putExtra("CardIndex", viewPager.getCurrentItem()));
    }

    private void deleteFlashCard() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentDeck.remove(currentDeck.currentCardIndex);
                        pageAdapter.notifyDataSetChanged();

                        // Remove edit and delete menu items when deck is empty
                        if (currentDeck.isEmpty()) {
                            invalidateOptionsMenu();
                        }

                        Settings.saveData(FlashcardActivity.this);
                    }
                }).setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * https://developer.android.com/samples/ImmersiveMode/project.html
     */
    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            getSupportActionBar().show();
            toggleFullscreen();
            isFullscreen = false;
        } else {
            super.onBackPressed();
        }
    }

    public static class FlashcardListActivity extends AppCompatActivity {

        private boolean updateOnResume;
        private FlashcardAdapter adapter;

        @Override
        protected void onResume() {
            super.onResume();
            if (updateOnResume) {
                adapter.notifyDataSetChanged();
                updateOnResume = false;
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_flashcard_list);
            Settings.loadData(this);
            setTitle(currentDeck.getTitle());
            adapter = new FlashcardAdapter(this);
            ((ListView) findViewById(R.id.flashcards_listview)).setAdapter(adapter);
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
                    updateOnResume = true;
                    startActivity(new Intent(this, AddEditActivity.class)
                            .putExtra("EditMode", false)
                            .putExtra("DeckIndex", Deck.currentDeckIndex));
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        public class FlashcardAdapter extends ArrayAdapter {
            public FlashcardAdapter(Context context) {
                super(context, 0, currentDeck);
            }

            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Create the layout
                View listItem = convertView;
                if (listItem == null) {
                    listItem = LayoutInflater.from(getContext()).inflate(
                            R.layout.flashcard_item, parent, false);
                }

                // Add the text to the textview
                ((TextView) listItem.findViewById(R.id.flashcard_item_text))
                        .setText(currentDeck.get(position).getSideA());

                // Event for when an item is clicked
                listItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDeck.currentCardIndex = position;
                        onNavigateUp();
                    }
                });

                return listItem;
            }
        }
    }
}
