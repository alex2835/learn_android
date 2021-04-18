package com.example.finalwork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalwork.databinding.FragmentListBinding

class ItemToTouchCallBack(var mNotesViewModel: NotesViewModel, var adapter: MainAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
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
        mNotesViewModel.Remove(viewHolder.adapterPosition)
        adapter.notifyDataSetChanged()
    }
}

class ListFragment : Fragment(), OnNoteListener
{
    private lateinit var binding: FragmentListBinding
    private lateinit var mMainAdapter: MainAdapter
    private val mNotesViewModel: NotesViewModel by activityViewModels()
    private lateinit var mItemToTouchCallBack: ItemToTouchCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainAdapter = MainAdapter(mNotesViewModel.mNotes.value!!, this)
        mNotesViewModel.mAdapter = mMainAdapter
        mItemToTouchCallBack = ItemToTouchCallBack(mNotesViewModel, mMainAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentListBinding>(
            inflater, R.layout.fragment_list, container, false)

        binding.addElemButton.setOnClickListener {
            mNotesViewModel.mPosition = -1
            var dialog = EditNoteFragment()
            dialog.show(childFragmentManager, "create")
        }
        binding.mainRecyclerView.adapter = mMainAdapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val itemTouchHelper = ItemTouchHelper(mItemToTouchCallBack)
        itemTouchHelper.attachToRecyclerView(binding.mainRecyclerView)

        return binding.root
    }

    override fun OnNoteClick(position: Int) {
        mNotesViewModel.mPosition = position
        var dialog = EditNoteFragment()
        dialog.show(childFragmentManager, "change")
    }

}