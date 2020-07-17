package com.example.todolist.todolist

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.todolist.database.Note
import com.example.todolist.database.TodoDatabaseDao
import kotlinx.coroutines.*

class TodoListViewModel (
    val database: TodoDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private val notes = database.getAllNotes()


    fun navigatetodtails(v: View)
    {
        uiScope.launch {
            val kity = Note()
            insert(kity)
            v.findNavController()
                .navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment(kity.noteId))
        }
    }


    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            database.update(note)
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.insert(note)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}