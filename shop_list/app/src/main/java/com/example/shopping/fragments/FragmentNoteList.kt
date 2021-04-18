package com.example.shopping.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.adapters.NoteListAdapter
import com.example.shopping.adapters.OnItemClickListener
import com.example.shopping.data.Note
import com.example.shopping.databinding.ListOfNotesBinding
import com.example.shopping.dialogs.DialogNewNote
import com.example.shopping.viewmodel.ViewModelListManager

class NoteToTouchCallBack(var notes: MutableList<Note>, var adapter: NoteListAdapter)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
{
    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
    ): Boolean
    {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
    {
        notes.removeAt(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
    }
}

class FragmentNoteList() : Fragment(), OnItemClickListener
{
    private val listManager: ViewModelListManager by activityViewModels()
    private lateinit var itemToTouchCallBack: NoteToTouchCallBack
    private lateinit var noteLitAdapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteLitAdapter = NoteListAdapter(listManager.getNoteListByName(listManager.activeListName.value!!), this)
        itemToTouchCallBack = NoteToTouchCallBack(listManager.getNoteListByName(listManager.activeListName.value!!), noteLitAdapter)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ListOfNotesBinding>(
                inflater, R.layout.list_of_notes, container, false)

        var recyclerView           = binding.recyclerViewNotes
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter       = noteLitAdapter

        val itemTouchHelper = ItemTouchHelper(itemToTouchCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewNotes)

        binding.buttonNewNote.setOnClickListener {
            DialogNewNote(listManager.getNoteListByName(listManager.activeListName.value!!), listManager.getAvailableItemList())
                    .show(childFragmentManager, "New list")
        }
        return binding.root
    }

    override fun OnNoteClick(v: View, position: Int) {
        val listName = listManager.getListOfLists().get(position)
    }
}