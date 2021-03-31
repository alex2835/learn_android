package com.example.finalwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


public interface OnNoteListener
{
    fun OnNoteClick(position: Int)
}

class MainAdapter(var mNotes: MutableList<Note>, var note_listener: OnNoteListener) : RecyclerView.Adapter<CustomViewHolder>()
{
    override fun getItemViewType(position: Int): Int
    {
        return 0
    }

    override fun getItemCount(): Int
    {
        return mNotes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.listview_row, parent, false)
        return CustomViewHolder(cellForRow, note_listener)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.FillNote(mNotes.get(position))
    }

}

class CustomViewHolder(var view: View, var note_listener: OnNoteListener) : RecyclerView.ViewHolder(view),
    View.OnClickListener
{
    init {
        view.setOnClickListener(this)
    }

    fun FillNote(note: Note)
    {
        var textView_date  = view.findViewById<TextView>(R.id.textView_date)
        textView_date.text = note.date.format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)).toString()

        var textView_systolic  = view.findViewById<TextView>(R.id.textView_systolic)
        var textView_diastolic = view.findViewById<TextView>(R.id.textView_diastolic)
        var textView_pulse     = view.findViewById<TextView>(R.id.textView_pulse)

        textView_systolic.text  = "Systolic pressure: ${note.systolicPressure}"
        textView_diastolic.text = "Diastolic pressure: ${note.diastolicPressure}"
        textView_pulse.text     = "Pulse: ${note.pulse}"
    }

    override fun onClick(v: View?)
    {
        note_listener.OnNoteClick(adapterPosition)
    }

}