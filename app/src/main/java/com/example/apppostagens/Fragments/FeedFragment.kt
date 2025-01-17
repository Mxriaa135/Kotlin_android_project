package com.example.apppostagens.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppostagens.Adapter.PostAdapter
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R


class FeedFragment : Fragment() {

    private lateinit var list: RecyclerView
    private var listPosts: MutableList<Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        var view : View = inflater.inflate(R.layout.fragment_feed, container, false)

        createPost()
        list = view.findViewById(R.id.list)
        var adapter = PostAdapter(listPosts)
        var layoutManager = LinearLayoutManager(context)

        list.layoutManager = layoutManager
        list.setHasFixedSize(true)
        list.adapter = adapter

        return view
    }

    fun createPost(){
        // Criando o usuário teste
        var user1 = User("Amelia", R.drawable.black)
        //Criando os posts testes
        var post1 = Post(1,"The beautiful sunset!", R.drawable.sunset, "01-02-2025", user1, user1)
        var post2 = Post(2,"My sweet cat <3", R.drawable.cat, "01-08-2025", user1, user1)
        listPosts.add(post1)
        listPosts.add(post2)
    }

}