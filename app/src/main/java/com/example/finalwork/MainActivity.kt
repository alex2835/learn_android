package com.example.finalwork

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalwork.databinding.ActivityMainBinding
import java.time.LocalDateTime

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    // Adapter for recycler view
    private var mMainAdapter : MainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Deserialization and data setting
        var temp_array = arrayOf<Note>(Note(LocalDateTime.now(), 1, 1, 1),
                                       Note(LocalDateTime.now(), 2, 2, 2))
        mMainAdapter.SetNotes(temp_array)

        // Recycler view
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.adapter = mMainAdapter

        // Bottom navigation
        binding.bottomNavigation.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.bottom_nav_list -> {
                    //Toast.makeText(applicationContext, "List", Toast.LENGTH_SHORT).show()
                }
                R.id.bottom_nav_chart-> {
                    //Toast.makeText(applicationContext, "Statistics", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }


}