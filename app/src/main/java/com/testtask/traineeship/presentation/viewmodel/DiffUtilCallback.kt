package com.testtask.traineeship.presentation.viewmodel
import androidx.recyclerview.widget.DiffUtil
import com.testtask.traineeship.domain.model.Contact

class DiffUtilCallback(
    private val oldItems: List<Contact>,
    private val newItems: List<Contact>
): DiffUtil.Callback()  {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldItems[oldItemPosition].number == newItems[newItemPosition].number
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem.lastName == newItem.lastName && oldItem.name == newItem.name
    }
}