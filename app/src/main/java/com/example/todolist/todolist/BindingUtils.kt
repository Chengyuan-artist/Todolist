package com.example.todolist.todolist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.todolist.database.Note

@BindingAdapter("textViewDetailsTitle")
fun TextView.setTextViewDetailsTitle(item: Note?) {
    item?.let {
        text=item.title
    }
}

@BindingAdapter("textViewItemTime")
fun TextView.setTextViewItemTime(item:Note?){
    item?.let {
        //waiting to add the time feature
        text=item.creatingtime.toString()
    }
}