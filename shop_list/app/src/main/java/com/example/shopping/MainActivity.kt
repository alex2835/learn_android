package com.example.shopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.shopping.viewmodel.ViewModelListManager

class MainActivity : AppCompatActivity()
{
    private val listManager: ViewModelListManager by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        listManager.serialize() // Save state
    }

}