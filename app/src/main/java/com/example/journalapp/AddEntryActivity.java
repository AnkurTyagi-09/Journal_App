package com.example.journalapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddEntryActivity extends AppCompatActivity {
    private TextInputLayout titleInputLayout, contentInputLayout;
    private EditText titleInput, contentInput;
    private Spinner moodSpinner;
    private Button saveButton;
    private JournalDatabase db;
    private JournalEntry entryToEdit;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private static final String PREFS_NAME = "JournalPrefs";
    private static final String PREF_BACKGROUND_COLOR = "background_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        // Initialize UI components
        titleInputLayout = findViewById(R.id.titleInputLayout);
        titleInput = findViewById(R.id.titleInput);
        contentInputLayout = findViewById(R.id.contentInputLayout);
        contentInput = findViewById(R.id.contentInput);
        moodSpinner = findViewById(R.id.moodSpinner);
        saveButton = findViewById(R.id.saveButton);
        db = JournalDatabase.getDatabase(this);

        // Apply background color preference
        applyBackgroundColor();

        // Set up mood spinner with emojis
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);

        // Check if editing an existing entry
        int entryId = getIntent().getIntExtra("ENTRY_ID", -1);
        if (entryId != -1) {
            executor.execute(() -> {
                entryToEdit = db.journalDao().getEntryById(entryId);
                runOnUiThread(() -> {
                    if (entryToEdit != null) {
                        titleInput.setText(entryToEdit.getTitle());
                        contentInput.setText(entryToEdit.getContent());
                        // Match mood without emoji
                        String moodWithoutEmoji = entryToEdit.getMood().replaceAll("[^a-zA-Z]", "");
                        String[] moods = getResources().getStringArray(R.array.mood_array);
                        for (int i = 0; i < moods.length; i++) {
                            if (moods[i].replaceAll("[^a-zA-Z]", "").equals(moodWithoutEmoji)) {
                                moodSpinner.setSelection(i);
                                break;
                            }
                        }
                    }
                });
            });
        }

        // Save button click listener
        saveButton.setOnClickListener(v -> saveEntry());
    }

    private void applyBackgroundColor() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int backgroundColor = prefs.getInt(PREF_BACKGROUND_COLOR, Color.rgb(250, 250, 250)); // Default: Light Grey
        findViewById(R.id.rootLayout).setBackgroundColor(backgroundColor);
    }

    private void saveEntry() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();
        String mood = moodSpinner.getSelectedItem().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (title.isEmpty()) {
            titleInputLayout.setError("Title is required");
            return;
        } else {
            titleInputLayout.setError(null);
        }

        if (content.isEmpty()) {
            contentInputLayout.setError("Content is required");
            return;
        } else {
            contentInputLayout.setError(null);
        }

        executor.execute(() -> {
            if (entryToEdit == null) {
                // New entry
                JournalEntry entry = new JournalEntry(title, content, mood, date);
                db.journalDao().insert(entry);
            } else {
                // Update existing entry
                entryToEdit.setTitle(title);
                entryToEdit.setContent(content);
                entryToEdit.setMood(mood);
                entryToEdit.setDate(date);
                db.journalDao().update(entryToEdit);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        });
    }
}