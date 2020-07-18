package com.example.todolist.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.database.Note

class TodoListAdapter : ListAdapter<Note, TodoListAdapter.ViewHolder>(NoteDiffCallback()){


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View):RecyclerView.ViewHolder(itemView){
        val title:TextView=itemView.findViewById(R.id.textViewDetailsTitle)
        val time:TextView=itemView.findViewById(R.id.textViewItemTime)

        fun bind(
            item: Note
        ) {
            val res = itemView.context.resources
            title.text = item.title
            time.text = item.creatingtime.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_note, parent, false)
                return ViewHolder(view)
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.noteId==newItem.noteId
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem==newItem
        }

    }
}