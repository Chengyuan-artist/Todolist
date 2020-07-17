package com.example.todolist.tododetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.database.TodoDatabaseDao

class TodoDetailsViewModelFactory(
    private val dataSource: TodoDatabaseDao,
    private val application: Application,
    val noteId: Long
) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoDetailsViewModel::class.java)) {
                return TodoDetailsViewModel(dataSource, application,noteId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}

