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

    private var tonight = MutableLiveData<Note?>()

    private val notes = database.getAllNotes()

    private var _showSnackbarEvent = MutableLiveData<Boolean>()

//    var item:
    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    private val _navigateToSleepQuality = MutableLiveData<Note>()
    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */

    val livenote = MediatorLiveData<Note>()

    init {
        livenote.addSource(database.getLiveNote(noteId),livenote::setValue)
    }



    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }
    /**
     * If this is non-null, immediately navigate to [SleepQualityFragment] and call [doneNavigating]
     */
    val navigateToSleepQuality: LiveData<Note>
        get() = _navigateToSleepQuality

    /**
     * Call this immediately after navigating to [SleepQualityFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }


    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */


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

    /**
     * Executes when the START button is clicked.
     */


    /**
     * Executes when the STOP button is clicked.
     */

    /**
     * Executes when the CLEAR button is clicked.
     */

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}