package com.example.shopping.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.adapters.ListOfListsAdapter
import com.example.shopping.adapters.NoteListAdapter
import com.example.shopping.adapters.OnItemClickListener
import com.example.shopping.data.Note
import com.example.shopping.databinding.ListOfListsBinding
import com.example.shopping.dialogs.DialogNewList
import com.example.shopping.dialogs.DialogNewName
import com.example.shopping.viewmodel.ViewModelListManager

class ListItemToTouchCallBack(var listManager: ViewModelListManager, var adapter: ListOfListsAdapter)
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
        val listName = listManager.getListOfLists()[viewHolder.adapterPosition]
        listManager.removeNoteListByName(listName)
        listManager.getListOfLists().removeAt(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
    }
}

public interface OnItemCopyClickListener
{
    fun OnCopyClick(v: View, position: Int)
}

class FragmentListOfLists : Fragment(), OnItemClickListener, OnItemCopyClickListener
{
    private val listManager: ViewModelListManager by activityViewModels()
    private lateinit var itemToTouchCallBack: ListItemToTouchCallBack
    private lateinit var itemLitAdapter: ListOfListsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemLitAdapter = ListOfListsAdapter(listManager.getListOfLists(), this, this)
        itemToTouchCallBack = ListItemToTouchCallBack(listManager, itemLitAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ListOfListsBinding>(
            inflater, R.layout.list_of_lists, container, false)

        var recyclerView           = binding.recyclerViewLists
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter       = itemLitAdapter

        val itemTouchHelper = ItemTouchHelper(itemToTouchCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewLists )

        binding.buttonNewList.setOnClickListener {
            DialogNewList(listManager.getListOfLists()).show(childFragmentManager, "New list")
        }

        binding.buttonNewName.setOnClickListener {
            DialogNewName(listManager.getAvailableItemList(), -1).show(childFragmentManager, "New name")
        }

        return binding.root
    }

    override fun OnNoteClick(v: View, position: Int) {
        listManager.activeListName.value = listManager.getListOfLists().get(position)
        Navigation.findNavController(v).navigate(R.id.action_fragmentListManager_to_fragmentNoteList)
    }

    override fun OnCopyClick(v: View, position: Int) {
        var list = listManager.getListOfLists()
        val name = list.get(position)
        var new_name = name + " copy"

        if (!list.contains(new_name)) {
            list.add(new_name)
            var note_list = listManager.getNoteListByName(new_name)
            for (note in listManager.getNoteListByName(name))
                note_list.add(note)

            itemLitAdapter.notifyDataSetChanged()
        }
    }

}