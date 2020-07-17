package com.example.todolist.tododetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.database.Note
import com.example.todolist.database.TodoDatabase
import com.example.todolist.databinding.FragmentTodoDetailsBinding

class TodoDetailsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTodoDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_todo_details, container, false)

        val application= requireNotNull(this.activity).application
        val database= TodoDatabase.getInstance(application).todoDatabaseDao

        val arguments=TodoDetailsFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory=TodoDetailsViewModelFactory(database,application,arguments.noteId)
        val viewModel= ViewModelProvider(this,viewModelFactory).get(TodoDetailsViewModel::class.java)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this



        return binding.root
    }

}