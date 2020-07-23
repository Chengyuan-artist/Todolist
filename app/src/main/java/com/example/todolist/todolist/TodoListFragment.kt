package com.example.todolist.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                this.findNavController().navigate(
                    TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment(note)
                )
                viewModel.onDetailsNavigated()
            }
        })

        binding.noteList.adapter = adapter


        viewModel.notes.observe(viewLifecycleOwner, Observer {

            noteList = if (it.isNotEmpty()) {
                it.toMutableList()
            } else {
                mutableListOf<Note>()
            }

            adapter.submitList(it)


        })

        binding.buttonAdd.setOnClickListener { v: View ->

            viewModel.navigatetodtails(v)
        }

        val itemHelperCallback = ItemHelperCallback(this)
        val itemHelper = ItemHelper(itemHelperCallback)
        val recyclerView = binding.noteList
        itemHelper.attachToRecyclerView(recyclerView)

        return binding.root

    }

    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }


    override fun onMove(sourcePosition: Int, targetPosition: Int): Boolean {
        if (noteList != null) {
            uiScope.launch {

                noteList!!.swap(sourcePosition, targetPosition)

                var num = noteList!!.size.toLong()


                for (x in noteList!!) {
                    x.importance = num
                    num -= 1
                    withContext(Dispatchers.IO){
                        database.update(x)
                    }
                }


                noteList!!.sortByDescending { note ->
                    note.importance
                }

                adapter.submitList(noteList!!.toList())


            }




            return true
        }
        return false
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


}