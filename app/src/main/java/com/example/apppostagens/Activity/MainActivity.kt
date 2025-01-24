package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apppostagens.Fragments.FeedFragment
import com.example.apppostagens.R
import com.example.apppostagens.databinding.ActivityMainBinding
import com.example.apppostagens.databinding.NavigationBottomBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_container_Main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnve)
        bottomNavigationView.setupWithNavController(navController)

    }

}