package com.example.todolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
        @PrimaryKey(autoGenerate = true)
        var noteId: Long = 0L,

        @ColumnInfo(name = "title")
        var title: String= "无标题",

        @ColumnInfo(name = "content")
        var notecontent: String = "空",

        @ColumnInfo(name = "creatingtime")
        var creatingtime: Long = System.currentTimeMillis()

)
