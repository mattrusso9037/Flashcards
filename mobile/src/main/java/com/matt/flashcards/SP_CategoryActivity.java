package com.matt.flashcards;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.Deck;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

import static com.matt.flashcards.R.id.sync_wear;


public class SP_CategoryActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private DeckAdapter adapter;
    private DrawerLayout drawer;
    private MenuItem syncItem;
    private NavigationView navView;
    private Menu menu;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Settings.loadData(this);

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
                                        .setMessage("Are you sure you want to overwrite all your data with dummy data?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Settings.loadDummyData();
                                                Settings.saveData(SP_CategoryActivity.this);
                                                adapter.notifyDataSetChanged();
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
                    .setTitle("Create deck:")
                    .setView(inflater)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String deckTitle = dialogName.getText().toString();
                            if (deckTitle.isEmpty()) {
                                new AlertDialog.Builder(SP_CategoryActivity.this)
                                        .setTitle("Error")
                                        .setMessage("You can't create a deck without a title")
                                        .setPositiveButton("Ok", null)
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
                    }).setNegativeButton("Cancel", null)
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
        Toast.makeText(this, "Connection Lost", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("wear", "disconnected from GoogleApiClient");
    }

    // *

    private void syncWear() {
        WearTask secondaryWearTask = new WearTask(this, syncItem);
        secondaryWearTask.execute();
        drawer.closeDrawer(GravityCompat.START);
        Toast.makeText(this, "Synced With Wear", Toast.LENGTH_SHORT).show();

    }


}
