package com.example.shopping.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.shopping.R
import com.example.shopping.data.Note
import com.example.shopping.databinding.DialogNewNoteBinding
import com.example.shopping.viewmodel.ItemNameWithMetrics

class DialogNewNote(var listOfNotes: MutableList<Note>,
                    var availableNamesWithMetrics: MutableList<ItemNameWithMetrics>)
    : DialogFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DialogNewNoteBinding>(
                inflater, R.layout.dialog_new_note, container, false)

        var availableNames = mutableListOf<String>()
        for (note in availableNamesWithMetrics)
            availableNames.add(note.first)

        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.spiner_item, availableNames)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerName.adapter = arrayAdapter

        if (availableNamesWithMetrics.size > 0) {
            var arrayAdapter = ArrayAdapter(requireContext(), R.layout.spiner_item, availableNamesWithMetrics[0].second)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerMetrics.adapter = arrayAdapter
        }

        binding.spinnerName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val id = binding.spinnerName.selectedItemPosition
                var arrayAdapter = ArrayAdapter(requireContext(), R.layout.spiner_item, availableNamesWithMetrics[id].second)
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerMetrics.adapter = arrayAdapter
            }

        }

        binding.buttonAdd.setOnClickListener {
            try {
                val name   = binding.spinnerName.getSelectedItem().toString()
                val amount = Integer.parseInt(binding.editTextAmount.text.toString())
                val metric = binding.spinnerMetrics.getSelectedItem().toString()
                listOfNotes.add(Note(name, amount, metric))
                dismiss()
            }
            catch (e: Exception) {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}

