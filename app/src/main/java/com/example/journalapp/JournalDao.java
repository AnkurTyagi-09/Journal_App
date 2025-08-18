package com.example.journalapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    List<JournalEntry> getAllEntries();

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    JournalEntry getEntryById(int id);

    @Insert
    void insert(JournalEntry entry);

    @Update
    void update(JournalEntry entry);

    @Delete
    void delete(JournalEntry entry);
}