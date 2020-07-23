package com.example.todolist.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.database.Note
import com.example.todolist.databinding.ListNoteBinding

class TodoListAdapter(val clickListener:NoteListener) : ListAdapter<Note, TodoListAdapter.ViewHolder>(NoteDiffCallback()){


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!,clickListener)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListNoteBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: Note,
            clickListener: NoteListener
        ) {
//            val res = itemView.context.resources
            binding.note=item
            binding.clickListener=clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding= ListNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
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

class NoteListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(note: Note) = clickListener(note.noteId)
}