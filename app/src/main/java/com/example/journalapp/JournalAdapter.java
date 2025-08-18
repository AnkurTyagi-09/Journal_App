package com.example.journalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private List<JournalEntry> entries;
    private OnItemClickListener editListener;
    private OnItemClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(JournalEntry entry);
    }

    public JournalAdapter(List<JournalEntry> entries, OnItemClickListener editListener, OnItemClickListener deleteListener) {
        this.entries = entries;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journal_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JournalEntry entry = entries.get(position);
        holder.titleTextView.setText(entry.getTitle());
        holder.dateTextView.setText(entry.getDate());
        holder.contentTextView.setText(entry.getContent());

        // Set mood text with emoji and background color
        String mood = entry.getMood();
        holder.moodTextView.setText(mood);
        int moodColor;
        switch (mood.replaceAll("[^a-zA-Z]", "")) { // Remove emoji for comparison
            case "Happy":
                moodColor = R.color.mood_happy;
                break;
            case "Sad":
                moodColor = R.color.mood_sad;
                break;
            case "Neutral":
                moodColor = R.color.mood_neutral;
                break;
            case "Excited":
                moodColor = R.color.mood_excited;
                break;
            case "Stressed":
                moodColor = R.color.mood_stressed;
                break;
            default:
                moodColor = R.color.primary;
        }
        holder.moodTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), moodColor));
        holder.moodTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));

        holder.itemView.setOnClickListener(v -> editListener.onItemClick(entry));
        holder.itemView.setOnLongClickListener(v -> {
            deleteListener.onItemClick(entry);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, dateTextView, moodTextView, contentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            moodTextView = itemView.findViewById(R.id.moodTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}