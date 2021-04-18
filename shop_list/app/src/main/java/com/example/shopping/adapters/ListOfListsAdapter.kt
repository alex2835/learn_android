package com.example.shopping.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.fragments.OnItemCopyClickListener

class ListOfListsAdapter(var listNames: MutableList<String>,
                         val item_listener: OnItemClickListener,
                         val copy_listener: OnItemCopyClickListener?)
    : RecyclerView.Adapter<ListOfListsViewHolder>()
{
    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return listNames.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfListsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_of_lists_item, parent, false)
        return ListOfListsViewHolder(cellForRow, item_listener, copy_listener)
    }

    override fun onBindViewHolder(holder: ListOfListsViewHolder, position: Int) {
        holder.fillNote(listNames.get(position))
    }

}

class ListOfListsViewHolder(var view: View,
                            var item_listener: OnItemClickListener,
                            val copy_listener: OnItemCopyClickListener?)
    : RecyclerView.ViewHolder(view), View.OnClickListener
{
    init {
        view.setOnClickListener(this)
        view.findViewById<Button>(R.id.buttonCopy).setOnClickListener {
            copy_listener!!.OnCopyClick(view, adapterPosition)
        }
    }

    fun fillNote(name: String) {
        view.findViewById<TextView>(R.id.textViewListOfListsName).setText(name)
    }

    override fun onClick(v: View?) {
        item_listener.OnNoteClick(v!!, adapterPosition)
    }

}