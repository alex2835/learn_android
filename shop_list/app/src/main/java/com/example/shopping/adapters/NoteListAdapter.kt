package com.example.shopping.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.data.Note

class NoteListAdapter(private val shopNotesList: List<Note>, val item_listener: OnItemClickListener)
    : RecyclerView.Adapter<ShopItemViewHolder>()
{
    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return shopNotesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val note_view = layoutInflater.inflate(R.layout.note_item, parent, false)
        return ShopItemViewHolder(note_view, item_listener)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.FillNote(shopNotesList[position])
    }

}

class ShopItemViewHolder(var view: View, val item_listener: OnItemClickListener)
    : RecyclerView.ViewHolder(view), View.OnClickListener
{
    init {
        view.setOnClickListener(this)
    }

    fun FillNote(note: Note) {
        val text = "${note.Name} ${note.Value} ${note.Metrics}"
        view.findViewById<CheckBox>(R.id.checkBox).setText(text)
    }

    override fun onClick(v: View?)
    {
        item_listener.OnNoteClick(v!!, adapterPosition)
    }

}