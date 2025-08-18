package com.example.journalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView journalRecyclerView;
    private FloatingActionButton addEntryButton, settingsButton;
    private JournalAdapter adapter;
    private final List<JournalEntry> entries = new ArrayList<>();
    private JournalDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private static final String PREFS_NAME = "JournalPrefs";
    private static final String PREF_LAYOUT = "layout";
    private static final String PREF_BACKGROUND_COLOR = "background_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init
        journalRecyclerView = findViewById(R.id.journalRecyclerView);
        addEntryButton = findViewById(R.id.addEntryButton);
        db = JournalDatabase.getDatabase(this);

        // Apply saved UI prefs and set adapter
        applyPreferences();
        adapter = new JournalAdapter(entries, this::editEntry, this::deleteEntry);
        journalRecyclerView.setAdapter(adapter);

        // Add new entry
        addEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // ✅ Hook up the layout button (if present in activity_main.xml)
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class))
        );

    }

    // App bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle app bar menu clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) { // <-- menu item id
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyPreferences();
        loadEntries();
    }

    private void applyPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String layout = prefs.getString(PREF_LAYOUT, "Linear");
        int backgroundColor = prefs.getInt(PREF_BACKGROUND_COLOR, Color.rgb(250, 250, 250));

        if ("Grid".equals(layout)) {
            journalRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            journalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        findViewById(R.id.coordinatorLayout).setBackgroundColor(backgroundColor);
    }

    private void loadEntries() {
        executor.execute(() -> {
            List<JournalEntry> newEntries = db.journalDao().getAllEntries();
            runOnUiThread(() -> {
                entries.clear();
                entries.addAll(newEntries);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private void editEntry(JournalEntry entry) {
        Intent intent = new Intent(this, AddEntryActivity.class);
        intent.putExtra("ENTRY_ID", entry.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void deleteEntry(JournalEntry entry) {
        executor.execute(() -> {
            db.journalDao().delete(entry);
            runOnUiThread(this::loadEntries);
            runOnUiThread(() ->
                    Snackbar.make(journalRecyclerView, "Entry deleted", Snackbar.LENGTH_LONG).show()
            );
        });
    }
}
