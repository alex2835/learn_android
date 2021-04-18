package com.example.shopping.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.shopping.R
import com.example.shopping.databinding.DialogNewListBinding


class DialogNewList(var listOfLists: MutableList<String>): DialogFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<DialogNewListBinding>(
            inflater, R.layout.dialog_new_list, container, false)

        binding.buttonAdd.setOnClickListener {
            val input_text = binding.editTextNewName.text.toString()
            if (input_text.isEmpty()) {
                Toast.makeText(context, "name is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (listOfLists.contains(input_text)) {
                Toast.makeText(context, "name is already exist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listOfLists.add(input_text)
            dismiss()
        }

        return binding.root
    }

}