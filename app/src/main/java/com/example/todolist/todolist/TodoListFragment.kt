package com.example.todolist.todolist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.todolist.R
import com.example.todolist.database.Note
import com.example.todolist.database.TodoDatabase
import com.example.todolist.database.TodoDatabaseDao
import com.example.todolist.databinding.FragmentTodoListBinding
import kotlinx.coroutines.*


class TodoListFragment : Fragment(), OnItemTouchCallBackListener {

    private var fragmentModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + fragmentModelJob)

    private var noteList: MutableList<Note>? = null

    lateinit var database: TodoDatabaseDao
    lateinit var adapter: TodoListAdapter
    lateinit var mymenu: Menu
    private var sortOption = 0
    private var lockByMove = false

    val TAG = "TodoListFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTodoListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_todo_list, container, false
        )


        val application = requireNotNull(this.activity).application
        database = TodoDatabase.getInstance(application).todoDatabaseDao
        val viewModelFactory = TodoListViewModelFactory(database, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TodoListViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = TodoListAdapter(NoteListener { noteId ->
            viewModel.onNoteClicked(noteId)
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer { noteId ->
            noteId?.let {
                this.findNavController().navigate(
                    TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment(noteId)
                )
                viewModel.onDetailsNavigated()
            }
        })

        binding.noteList.adapter = adapter


        viewModel.notes.observe(viewLifecycleOwner, Observer {

            Log.d(TAG, "Observer is being called")

            noteList = if (it.isNotEmpty()) {
                it.toMutableList()
            } else {
                mutableListOf<Note>()
            }

            noteList!!.sortByDescending { note ->
                note.importance
            }

            if (!lockByMove) {
                adapter.submitList(noteList!!.toList())
            }

            Log.d(TAG, "Observer is called")

        })

        binding.buttonAdd.setOnClickListener { v: View ->

            viewModel.navigatetodtails(v)
        }

        val itemHelperCallback = ItemHelperCallback(this)
        val itemHelper = ItemHelper(itemHelperCallback)
        val recyclerView = binding.noteList
        itemHelper.attachToRecyclerView(recyclerView)

        setHasOptionsMenu(true)

        return binding.root

    }


    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }

    private fun MutableList<Note>.sort(sortOption: Int) {
        when (sortOption) {
            0 -> this.sortByDescending { note ->
                note.importance
            }
            1 -> this.sortByDescending { note ->
                note.creatingtime
            }
            2 -> this.sortBy { note ->
                note.creatingtime
            }
            3 -> this.sortByDescending { note ->
                note.title
            }
            4 -> this.sortBy { note ->
                note.title
            }
        }
    }

    override fun onMove(sourcePosition: Int, targetPosition: Int): Boolean {
        if (noteList != null) {

            sortOption = 0


            noteList!!.swap(sourcePosition, targetPosition)

            val tmp = noteList!![sourcePosition].importance
            noteList!![sourcePosition].importance = noteList!![targetPosition].importance
            noteList!![targetPosition].importance = tmp

            adapter.submitList(noteList!!.toList())


            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        mymenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        return when (item.itemId) {


            R.id.menubytimedec -> {
                sortOption = 1
                updateView()
                true
            }
            R.id.menubytime -> {
                sortOption = 2
                updateView()
                true
            }
            R.id.menubychardec -> {
                sortOption = 3
                updateView()
                true
            }
            R.id.menubychar -> {
                sortOption = 4
                updateView()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateView() {
        noteList?.let {
            it.sort(sortOption)
            adapter.submitList(it.toList())

            var num = it.size.toLong()
            for (x in it) {
                x.importance = num
                num -= 1
            }
        }
    }

    override fun onSwipe(itemPosition: Int) {
        val toBeRemovedNote = noteList!![itemPosition]

        noteList?.removeAt(itemPosition)

        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(toBeRemovedNote)
            }
        }
    }

    override fun clearView() {

        uiScope.launch {
            withContext(Dispatchers.IO) {

                val copy = noteList!!.toList()
                lockByMove = true
                Log.d(TAG,"lock on")
                for (x in copy) {
                    Log.d(TAG, "Database is being updated")
                    database.update(x)
                    Log.d(TAG, "Database is updated")
                }
                lockByMove = false
                Log.d(TAG,"lock off")
            }
        }

    }


}