package com.matt.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Deck;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.matt.flashcards.R.id.sync_wear;
import static com.matt.flashcards.Settings.isFirstRun;

public class SP_CategoryActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private DeckAdapter adapter;
    private DrawerLayout drawer;
    private MenuItem syncItem;
    private NavigationView navView;
    private Menu menu;
    private Toast syncToast;
    private LayoutInflater inflater;
    private FrameLayout layout;
    private Button nextButton;
    private Button prevButton;
    private TabLayout tabLayout;
    private int tutorialCount;
    private AlertDialog show;

    @Override
    protected void onResume() {
        super.onResume();

        // Make sure the drawer is closed when returning from another activity
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_category);

        navView = findViewById(R.id.nav_view);
        menu = navView.getMenu();
        syncItem = menu.findItem(R.id.sync_wear);
        syncToast = syncToast.makeText(this, getResources().getString(R.string.synced), Toast.LENGTH_SHORT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Settings.loadData(this);

        // Show tutorial if it's the first run
        if (isFirstRun) {
            createTutorialView();
            isFirstRun = false;
        }

        adapter = new DeckAdapter(this, Settings.theDeckOfDecks, syncItem);
        ((GridView) findViewById(R.id.grd_mp_category)).setAdapter(adapter);

        // Event for the Fab
        findViewById(R.id.btn_sp_category).setOnClickListener(FabListener);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //wear *
        WearTask primaryWearTask = new WearTask(this, syncItem);
        primaryWearTask.execute();
        // *

        // Events for the drawer items
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle navigation view item clicks here.
                        switch (item.getItemId()) {
                            case R.id.nav_new_category:
                                FabListener.onClick(getCurrentFocus());
                                break;
                            case R.id.nav_load_dummy_data:
                                new AlertDialog.Builder(SP_CategoryActivity.this)
                                        .setTitle("Warning")
                                        .setMessage("Are you sure you want to overwrite all your data with sample data?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Settings.loadDummyData();
                                                Settings.saveData(SP_CategoryActivity.this);
                                                adapter.notifyDataSetChanged();
                                                syncWear();
                                                syncToast.cancel();
                                            }
                                        }).setNegativeButton("Cancel", null)
                                        .create().show();
                                break;
//                    case R.id.nav_settings:
//                        startActivity(new Intent(SP_CategoryActivity.this, SettingsActivity.class));
//                        break;
                            case sync_wear:
                                syncWear();
                                break;
                            case R.id.nav_run_tutorial:
                                createTutorialView();
                                break;
                            case R.id.nav_about:
                                startActivity(new Intent(SP_CategoryActivity.this, AboutActivity.class));
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private View.OnClickListener FabListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final View inflater = getLayoutInflater().inflate(R.layout.deck_dialog, null);
            final TextView dialogName = inflater.findViewById(R.id.deck_dialog_name);
            new AlertDialog.Builder(SP_CategoryActivity.this)
                    .setTitle(getResources().getString(R.string.create_deck))
                    .setView(inflater)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String deckTitle = dialogName.getText().toString();
                            if (deckTitle.isEmpty()) {
                                new AlertDialog.Builder(SP_CategoryActivity.this)
                                        .setTitle(getResources().getString(R.string.error))
                                        .setMessage(getResources().getString(R.string.error_msg_1))
                                        .setPositiveButton(getResources().getString(R.string.ok), null)
                                        .create().show();
                            } else {
                                Settings.theDeckOfDecks.add(new Deck(deckTitle));
                                adapter.notifyDataSetChanged();
                                Settings.saveData(SP_CategoryActivity.this);
                                WearTask newDeckWearTask = new WearTask(SP_CategoryActivity.this, syncItem);
                                newDeckWearTask.execute();
                                if (drawer.isDrawerOpen(GravityCompat.START)) {
                                    drawer.closeDrawer(GravityCompat.START, false);
                                }
                            }
                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel), null)
                    .create().show();
        }
    };

    //* GoogleApiClient
    @Override
    public void onConnected(Bundle bundle) {
        Log.i("wear", "connected to GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("wear", "connection failed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("wear", "disconnected from GoogleApiClient");
    }

    private void syncWear() {
        WearTask secondaryWearTask = new WearTask(this, syncItem);
        secondaryWearTask.execute();
        drawer.closeDrawer(GravityCompat.START);
        syncToast.show();
    }

    private void createTutorialView() {
        tutorialCount = 0;
        inflater = getLayoutInflater();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final View dialogLayout = inflater.inflate(R.layout.tutorial_layout, null);

        setTutorialItems(dialogLayout);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);

        show = builder.show();
        show.show();
        show.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels - 100,
                getResources().getDisplayMetrics().heightPixels - 500);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialCount++;
                if (tutorialCount < 6) {
                    TabLayout.Tab tab = tabLayout.getTabAt(tutorialCount);
                    tab.select();
                }
                runTutorial();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutorialCount != 0) {
                    if (tutorialCount > 4) {
                        nextButton.setText(getResources().getString(R.string.btn_next));
                    }
                    tutorialCount--;
                    TabLayout.Tab tab = tabLayout.getTabAt(tutorialCount);
                    tab.select();
                    runTutorial();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tutorialCount = tab.getPosition();
                runTutorial();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void runTutorial() {
        switch (tutorialCount) {
            case 0:
                layout.setBackgroundResource(R.drawable.screen_one);
                break;
            case 1:
                layout.setBackgroundResource(R.drawable.screen_two);
                break;
            case 2:
                layout.setBackgroundResource(R.drawable.screen_three);
                break;
            case 3:
                layout.setBackgroundResource(R.drawable.screen_four);
                break;
            case 4:
                layout.setBackgroundResource(R.drawable.screen_six);
                break;
            case 5:
                layout.setBackgroundResource(R.drawable.screen_five);
                nextButton.setText(getResources().getString(R.string.lets_go));
                break;
            case 6:
                show.dismiss();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }
    }

    private void setTutorialItems(View dialogLayout) {
        nextButton = dialogLayout.findViewById(R.id.tutorial_next_button);
        prevButton = dialogLayout.findViewById(R.id.tutorial_prev_button);
        layout = dialogLayout.findViewById(R.id.alert_body);
        tabLayout = dialogLayout.findViewById(R.id.tabDots);
    }
}
