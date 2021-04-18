package com.example.shopping.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R

class NewNameMetricsListAdapter(var metricsList: MutableList<String>, val item_listener: OnItemClickListener)
    : RecyclerView.Adapter<MetricItemViewHolder>()
{
    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return metricsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetricItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val note_view = layoutInflater.inflate(R.layout.metric_item, parent, false)
        return MetricItemViewHolder(note_view, item_listener)
    }

    override fun onBindViewHolder(holder: MetricItemViewHolder, position: Int) {
        holder.FillNote(metricsList[position])
    }

}

class MetricItemViewHolder(var view: View, val item_listener: OnItemClickListener)
    : RecyclerView.ViewHolder(view), View.OnClickListener
{
    init {
        view.setOnClickListener(this)
    }

    fun FillNote(metric_name: String) {
        view.findViewById<TextView>(R.id.textViewMetricName).setText(metric_name)
    }

    override fun onClick(v: View?)
    {
        item_listener.OnNoteClick(v!!, adapterPosition)
    }

}