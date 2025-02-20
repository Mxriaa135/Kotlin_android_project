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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FeedFragment : Fragment() {

    private lateinit var list: RecyclerView
    private var listPosts: MutableList<Post> = mutableListOf()
    private var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Post")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        var view : View = inflater.inflate(R.layout.fragment_feed, container, false)

        list = view.findViewById(R.id.list)
        var adapter = PostAdapter(listPosts)
        var layoutManager = LinearLayoutManager(context)

        list.layoutManager = layoutManager
        list.setHasFixedSize(true)
        list.adapter = adapter

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    post?.let { listPosts.add(it) }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })

        return view
    }
}