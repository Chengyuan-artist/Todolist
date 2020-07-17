package com.example.todolist.tododetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoDetailsBinding

class TodoDetailsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTodoDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_todo_details, container, false)

        return binding.root
    }

}