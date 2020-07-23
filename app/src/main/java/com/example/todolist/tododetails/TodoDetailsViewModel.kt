package com.example.todolist.tododetails

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.todolist.database.Note
import com.example.todolist.database.TodoDatabaseDao
import com.example.todolist.databinding.FragmentTodoDetailsBinding
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
            livenote.addSource(database.getLiveNote(noteId), livenote::setValue)
    }


    fun navigatetolist(
        binding: FragmentTodoDetailsBinding,
        v: View
    ) {
        val newNote = Note()
        newNote.noteId=noteId
        newNote.title = binding.editTextDetailsTitle.text.toString()
        newNote.notecontent = binding.editTextDetailsDetails.text.toString()
        uiScope.launch {
            update(newNote)
            v.findNavController()
                .navigate(TodoDetailsFragmentDirections.actionTodoDetailsFragmentToTodoListFragment())
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