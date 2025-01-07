package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppostagens.Adapter.PostAdapter
import com.example.apppostagens.Model.Post
import com.example.apppostagens.R

class MainActivity : AppCompatActivity() {

    private lateinit var list: RecyclerView
    private var listPosts: MutableList<Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        list = findViewById(R.layout.post)
        var adapter = PostAdapter(listPosts)
        var layoutManager = LinearLayoutManager(applicationContext)

        list.layoutManager = layoutManager
        list.setHasFixedSize(true)
        list.adapter = adapter

    }
}