package com.example.todolist.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.todolist.R
import com.example.todolist.database.Note
import com.example.todolist.database.TodoDatabase
import com.example.todolist.database.TodoDatabaseDao
import com.example.todolist.databinding.FragmentTodoListBinding
import kotlinx.coroutines.*


class TodoListFragment :Fragment() {

    private var fragmentModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + fragmentModelJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTodoListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_todo_list, container, false
        )

        val application = requireNotNull(this.activity).application

        val database = TodoDatabase.getInstance(application).todoDatabaseDao


        binding.buttonAdd.setOnClickListener { v: View ->

            uiScope.launch {
                val kity = Note()
                withContext(Dispatchers.IO) {
                    database.insert(kity)
                }
                v.findNavController()
                    .navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment(kity.noteId))
            }

        }
        return binding.root

    }
}