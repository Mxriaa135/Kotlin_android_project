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
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class FeedFragment : Fragment() {

    private lateinit var list: RecyclerView
    private var listPosts: MutableList<Post> = mutableListOf()
    private var reference: DatabaseReference = FirebaseConfiguration.getFirebaseReference().child("Feed")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        var view : View = inflater.inflate(R.layout.fragment_feed, container, false)

        list = view.findViewById(R.id.list)
        var layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager
        list.setHasFixedSize(true)

        var adapter = PostAdapter(listPosts)
        list.adapter = adapter

        loadData(adapter)

        return view
    }
    
    private fun loadData(adapter: PostAdapter){
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                listPosts.clear()

                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    post?.let { listPosts.add(it) }
                }

                adapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }
}