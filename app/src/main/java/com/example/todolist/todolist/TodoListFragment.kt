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
import com.example.todolist.databinding.FragmentTodoListBinding


class TodoListFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTodoListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_todo_list, container, false)
        binding.buttonAdd.setOnClickListener { v: View ->
            v.findNavController().navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment())
        }
        return binding.root
    }
}