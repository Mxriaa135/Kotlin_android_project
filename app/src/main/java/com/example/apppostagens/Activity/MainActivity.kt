package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.apppostagens.Fragments.FeedFragment
import com.example.apppostagens.R
import com.example.apppostagens.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, FeedFragment())
            .commit()

    }

}