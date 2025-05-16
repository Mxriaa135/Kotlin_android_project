package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppostagens.Adapter.CommentAdapter
import com.example.apppostagens.Model.Comment
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.example.apppostagens.databinding.ActivityCommentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CommentActivity : AppCompatActivity() {

    lateinit var binding: ActivityCommentBinding
    private lateinit var eventListener: ValueEventListener
    private lateinit var comment: Comment
    private lateinit var postId: String
    private lateinit var idUser: String
    private var listComments: MutableList<Comment> = mutableListOf()
    private lateinit var commentRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeComponents()

        val list = binding.listComment
        var layoutManager = LinearLayoutManager(this)
        list.layoutManager = layoutManager
        list.setHasFixedSize(true)

        var adapter = CommentAdapter(listComments)
        list.adapter = adapter
        loadData(adapter)

        binding.imageArrow.setOnClickListener {
            comment.add()
            comment.setText(binding.editTextComment.text.toString())
            comment.setIdPost(postId)
            comment.setIdUser(idUser)
            if(binding.editTextComment.text.isNotBlank()){
                comment.save()
                binding.editTextComment.text.clear()
            }
            adapter.notifyDataSetChanged()
        }

        binding.imageArrowBack.setOnClickListener {
            finish()
        }

    }

    private fun loadData(adapter: CommentAdapter){
        eventListener = commentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                listComments.clear()
                for(snapshot in ds.children){
                    val comment = snapshot.getValue(Comment::class.java)
                    comment?.let{
                        listComments.add(comment)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initializeComponents(){
        comment = Comment()
        idUser = UserFirebase.getCurrentUser()!!.uid
        postId = intent.getStringExtra("POST_ID")!!
        commentRef = FirebaseConfiguration.getFirebaseReference().child("Comments").child(postId)
    }

    override fun onDestroy() {
        super.onDestroy()
        commentRef.removeEventListener(eventListener)
    }

}