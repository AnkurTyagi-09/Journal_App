package com.example.journalapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "JournalPrefs";
    private static final String PREF_LAYOUT = "layout";
    private static final String PREF_BACKGROUND_COLOR = "background_color";

    private Spinner layoutSpinner;
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private View colorPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        layoutSpinner = findViewById(R.id.layoutSpinner);
        redSeekBar = findViewById(R.id.redSeekBar);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        blueSeekBar = findViewById(R.id.blueSeekBar);
        colorPreview = findViewById(R.id.colorPreview);

        // Spinner adapter
        ArrayAdapter<CharSequence> layoutAdapter = ArrayAdapter.createFromResource(
                this, R.array.layout_options, android.R.layout.simple_spinner_item
        );
        layoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutSpinner.setAdapter(layoutAdapter);

        // Load prefs
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLayout = prefs.getString(PREF_LAYOUT, "Linear");
        int savedColor = prefs.getInt(PREF_BACKGROUND_COLOR, Color.rgb(250, 250, 250));

        layoutSpinner.setSelection(layoutAdapter.getPosition(savedLayout));

        redSeekBar.setProgress(Color.red(savedColor));
        greenSeekBar.setProgress(Color.green(savedColor));
        blueSeekBar.setProgress(Color.blue(savedColor));
        updateColorPreview();

        // Persist layout choice
        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString(PREF_LAYOUT, layoutSpinner.getSelectedItem().toString()).apply();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Persist color as user slides
        SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorPreview();
                int color = Color.rgb(redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekBar.getProgress());
                prefs.edit().putInt(PREF_BACKGROUND_COLOR, color).apply();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };
        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            // Values are already persisted on change; here we just close
            finish();
        });

    }

    private void updateColorPreview() {
        int color = Color.rgb(redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekBar.getProgress());
        colorPreview.setBackgroundColor(color);
    }


}
