package com.example.todolist.todolist

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class ItemHelper(callback: ItemTouchHelper.Callback) : ItemTouchHelper(callback)

class ItemHelperCallback(private val onItemTouchCallbackListener: OnItemTouchCallBackListener) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return onItemTouchCallbackListener.onMove(
            viewHolder.adapterPosition,
            target.adapterPosition
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemTouchCallbackListener.onSwipe(viewHolder.adapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        onItemTouchCallbackListener.clearView()
    }
}

interface OnItemTouchCallBackListener {

    fun onMove(sourcePosition: Int, targetPosition: Int): Boolean

    fun onSwipe(itemPosition: Int)

    fun clearView()
}