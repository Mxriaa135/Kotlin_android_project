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
import com.example.apppostagens.Model.User
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
        createPost()
        list = findViewById(R.id.list)
        var adapter = PostAdapter(listPosts)
        var layoutManager = LinearLayoutManager(applicationContext)

        list.layoutManager = layoutManager
        list.setHasFixedSize(true)
        list.adapter = adapter

    }
    fun createPost(){
        // Criando o usuário teste
        var user1 = User("Amelia", R.drawable.black)
        //Criando os posts testes
        var post1 = Post("The beautiful sunset!", R.drawable.sunset, "01-02-2025", user1, user1)
        var post2 = Post("My sweet cat <3", R.drawable.cat, "01-08-2025", user1, user1)
        listPosts.add(post1)
        listPosts.add(post2)
    }
}