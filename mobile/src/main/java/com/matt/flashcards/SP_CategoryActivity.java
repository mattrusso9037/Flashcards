package com.matt.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class SP_CategoryActivity extends AppCompatActivity {

    private DeckAdapter adapter;
    private DrawerLayout drawer;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Settings.loadData();

        adapter = new DeckAdapter(this, Settings.theDeckOfDecks);
        ((GridView) findViewById(R.id.grd_mp_category)).setAdapter(adapter);

        // Event for the Fab
        findViewById(R.id.btn_sp_category).setOnClickListener(FabListener);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
                    case R.id.nav_settings:
                        startActivity(new Intent(SP_CategoryActivity.this, SettingsActivity.class));
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
                            Settings.theDeckOfDecks.add(new Deck(dialogName.getText().toString()));
                            adapter.notifyDataSetChanged();
                            Settings.saveData(getBaseContext());
                        }
                    }).setNegativeButton("Cancel", null)
                    .create().show();
        }
    };
}
