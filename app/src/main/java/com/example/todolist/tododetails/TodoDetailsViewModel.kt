package com.example.todolist.tododetails

import android.app.Application
import androidx.lifecycle.*
import com.example.todolist.database.Note
import com.example.todolist.database.TodoDatabaseDao
import kotlinx.coroutines.*

class TodoDetailsViewModel (
    val database: TodoDatabaseDao,
    application: Application,
    val noteId:Long
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val livenote = MediatorLiveData<Note>()

    init {
        livenote.addSource(database.getLiveNote(noteId),livenote::setValue)
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