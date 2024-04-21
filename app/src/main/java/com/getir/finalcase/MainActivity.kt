package com.getir.finalcase

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.getir.finalcase.presentation.product_list.ProductListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}