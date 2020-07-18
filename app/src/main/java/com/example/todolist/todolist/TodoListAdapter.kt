package com.example.todolist.todolist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.TextItemViewHolder
import com.example.todolist.database.Note

class TodoListAdapter :RecyclerView.Adapter<TextItemViewHolder>(){
    var data= listOf<Note>()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text=item.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        TODO("Not yet implemented")
    }
}