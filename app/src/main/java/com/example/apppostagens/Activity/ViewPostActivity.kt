package com.example.apppostagens.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.apppostagens.Model.Like
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.example.apppostagens.databinding.ActivityAddBinding
import com.example.apppostagens.databinding.ActivityViewPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ViewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPostBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var postRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var likeRef: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var imageLike : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeComponents()

        postRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue(Post::class.java)
                post?.let{

                    if (!isDestroyed && !isFinishing) {
                        Glide.with(this@ViewPostActivity)
                            .load(it.getImageUrl())
                            .placeholder(R.drawable.image)
                            .error(R.drawable.broken_image)
                            .into(binding.include.imagePost)
                    }

                    binding.include.description.text = it.getDescription()
                    binding.include.date.text = it.getDate()

                    binding.include.imageSave.setOnClickListener {
                        post.setSaved(!post.getSaved())
                        binding.include.imageSave.setImageResource(if (post.getSaved()) R.drawable.saved else R.drawable.save)
                    }

                    eventListener = likeRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(ds: DataSnapshot) {
                            val idCurrentUser = UserFirebase.getCurrentUser()!!.uid
                            val like = Like().apply {
                                setIdPost(post.getId())
                                setIdUser(idCurrentUser)
                            }
                            val gestureDetector = GestureDetector(binding.include.imagePost.context, object : GestureDetector.SimpleOnGestureListener() {
                                override fun onDoubleTap(e: MotionEvent): Boolean {
                                    like.save()
                                    imageLike.setImageResource(R.drawable.liked)
                                    return true
                                }
                            })

                            @SuppressLint("ClickableViewAccessibility")
                            if(ds.hasChild(idCurrentUser)) {
                                imageLike.setImageResource(R.drawable.liked)
                                imageLike.setOnClickListener {
                                    like.remove()
                                }
                            }
                            else{
                                imageLike.setImageResource(R.drawable.like)
                                imageLike.setOnClickListener {
                                    like.save()
                                }
                                binding.include.imagePost.setOnTouchListener { _, event ->
                                    gestureDetector.onTouchEvent(event)
                                    true
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    binding.include.imageComment.setOnClickListener {
                        val intent = Intent(this@ViewPostActivity, CommentActivity::class.java)
                        intent.putExtra("POST_ID", post.getId())
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user?.let{
                    if (!isDestroyed && !isFinishing) {
                        Glide.with(this@ViewPostActivity)
                            .load(it.getUserImage())
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(binding.include.userImage)
                    }


                    binding.include.userName.text = it.getUsername()
                    binding.include.userNameDescription.text = it.getUsername()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun initializeComponents(){
        val userId = intent.getStringExtra("userId").toString()
        val postId = intent.getStringExtra("postId").toString()
        firebaseRef = FirebaseConfiguration.getFirebaseReference()
        postRef = firebaseRef.child("Post").child(userId).child(postId)
        userRef = firebaseRef.child("User").child(userId)
        likeRef = firebaseRef.child("Likes").child(postId)
        imageLike = binding.include.imageLike
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::eventListener.isInitialized) {
            likeRef.removeEventListener(eventListener)
        }
    }
}