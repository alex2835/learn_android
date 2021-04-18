package com.example.shopping.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.BuildConfig
import com.example.shopping.R
import com.example.shopping.adapters.ListOfListsAdapter
import com.example.shopping.adapters.NewNameMetricsListAdapter
import com.example.shopping.adapters.OnItemClickListener
import com.example.shopping.databinding.DialogNewNameBinding
import com.example.shopping.viewmodel.ItemNameWithMetrics


class DialogNewName(var names_with_meatircs: MutableList<ItemNameWithMetrics>, val note_id: Int)
    : DialogFragment(), OnItemClickListener
{
    private var tempMetricsArray = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DialogNewNameBinding>(
                inflater, R.layout.dialog_new_name, container, false)

        recyclerView = binding.recyclerViewMetrics
        recyclerView .layoutManager = LinearLayoutManager(this.context)

        if (note_id == -1)
        {
            binding.editTextItemName.setText("")
            recyclerView.adapter = NewNameMetricsListAdapter(tempMetricsArray, this)
        }
        else if (note_id < names_with_meatircs.size && note_id >= 0)
        {
            tempMetricsArray = names_with_meatircs[note_id].second
            binding.editTextItemName.setText(names_with_meatircs[note_id].first)
            recyclerView.adapter = ListOfListsAdapter(tempMetricsArray, this, null)
        }
        else {
            if (BuildConfig.DEBUG) {
                error("Assertion failed")
            };
        }

        binding.buttonAddMetric.setOnClickListener {
            val metric_input = binding.editTextMetricName.text.toString()
            if (metric_input.isEmpty())
            {
                Toast.makeText(context, "metric field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            tempMetricsArray.add(metric_input)
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        binding.buttonAddNote.setOnClickListener {
            val item_name = binding.editTextItemName.text.toString()
            if (item_name.isEmpty())
            {
                Toast.makeText(context, "name field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (tempMetricsArray.size < 1)
            {
                Toast.makeText(context, "no metrics set", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (note_id == -1)
                names_with_meatircs.add(Pair(item_name, tempMetricsArray))
            else
                names_with_meatircs[note_id] = Pair(item_name, tempMetricsArray)

            dismiss()
        }

        return binding.root
    }

    override fun OnNoteClick(v: View, position: Int) {

    }

}