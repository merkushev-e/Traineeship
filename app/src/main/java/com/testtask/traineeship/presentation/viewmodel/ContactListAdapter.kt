package com.testtask.traineeship.presentation.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import coil.api.load
import coil.transform.CircleCropTransformation
import com.testtask.traineeship.R
import com.testtask.traineeship.domain.model.Contact

class ContactListAdapter(
) : RecyclerView.Adapter<ContactListAdapter.RecyclerItemViewHolder>() {


    private var data: MutableList<Contact> = mutableListOf()
    var listener: OnListItemClickListener? = null
    var onDeleteListener: OnListItemDeleteClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var fragment: Fragment? = null
    fun setData(newData: List<Contact>){
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data,newData))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_contact_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Contact) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                registerContextMenu(itemView)
                itemView.setOnLongClickListener {
                    onItemLongClickListener?.onItemLongClick(layoutPosition, data)
                    itemView.showContextMenu(20F, 20F)
                    false
                }
                with(itemView) {
                    findViewById<TextView>(R.id.tv_name).text = data.name
                    findViewById<TextView>(R.id.tv_last_name).text = data.lastName
                    findViewById<ImageView>(R.id.imageView).load("https://picsum.photos/id/${data.picId}/200/300"){
                        transformations(CircleCropTransformation())
                        build()
                    }
                    findViewById<ImageView>(R.id.edit_image_button).setOnClickListener { listener?.onItemClick(data) }
                    findViewById<ImageView>(R.id.delete_image_button).setOnClickListener {
                        removeItem()
                        onDeleteListener?.onItemDelete(data)
                    }
                }
            }
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }
    }

    private fun registerContextMenu(itemView: View) {
        fragment?.registerForContextMenu(itemView)

    }


    fun appendItem(contact: Contact){
        data.add(contact)
        notifyDataSetChanged()
    }


    fun interface OnListItemClickListener {
        fun onItemClick(data: Contact)
    }

    fun interface OnListItemDeleteClickListener{
        fun onItemDelete(data: Contact)
    }


    fun interface OnItemLongClickListener {
        fun onItemLongClick(position: Int, data: Contact)
    }


    fun removeOnPosition(layoutPosition: Int){
        data.removeAt(layoutPosition)
    }


}