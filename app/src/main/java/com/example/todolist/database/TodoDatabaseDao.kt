package com.example.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDatabaseDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM notes_table")
    fun clear()

    @Query("SELECT * from notes_table WHERE noteId = :key")
    fun get(key: Long): Note?

    @Query("SELECT * from notes_table WHERE noteId = :key")
    fun getLiveNote(key: Long): LiveData<Note>

    @Query("SELECT * FROM notes_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table ORDER BY noteId DESC LIMIT 1")
    fun getCurrentNote():LiveData<Note>
}