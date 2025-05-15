package com.example.apppostagens.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppostagens.Activity.CommentActivity
import com.example.apppostagens.Model.Like
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.Utils.UserFirebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PostAdapter(private val list: List<Post>) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])

    }
    override fun getItemCount(): Int = list.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val description: TextView = itemView.findViewById(R.id.description)
        val imagePost: ImageView = itemView.findViewById(R.id.imagePost)
        val imagelike: ImageView = itemView.findViewById(R.id.imageLike)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val imageSave: ImageView = itemView.findViewById(R.id.imageSave)
        val date: TextView = itemView.findViewById(R.id.date)
        val userNameDescription: TextView = itemView.findViewById(R.id.userNameDescription)
        val imageComment: ImageView = itemView.findViewById(R.id.imageComment)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post : Post){

            val databaseReferenceUser = FirebaseConfiguration.getFirebaseReference()
                .child("User")
                .child(post.getUserId())
            databaseReferenceUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        dataSnapshot.getValue(User::class.java)?.let {
                            userName.text = it.getUsername()
                            userNameDescription.text = it.getUsername()

                            Glide.with(itemView.context)
                                .load(it.getUserImage())
                                .placeholder(R.drawable.profile)
                                .error(R.drawable.profile)
                                .into(userImage)
                        }
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    error.toException().printStackTrace()
                }
            })

            val databaseReferencePost =  FirebaseConfiguration.getFirebaseReference()
                .child("Post")
                .child(post.getUserId())
                .child(post.getId())
            databaseReferencePost.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        dataSnapshot.getValue(Post::class.java)?.let {
                            Glide.with(itemView.context)
                                .load(it.getImageUrl())
                                .placeholder(R.drawable.image)
                                .error(R.drawable.broken_image)
                                .into(imagePost)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    error.toException().printStackTrace()
                }
            })

            description.text = post.getDescription()
            date.text = post.getDate()

            imageSave.setOnClickListener {
                post.setSaved(!post.getSaved())
                imageSave.setImageResource(if (post.getSaved()) R.drawable.saved else R.drawable.save)
            }

            val likeRef = FirebaseConfiguration.getFirebaseReference().child("Likes").child(post.getId())
            likeRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(ds: DataSnapshot) {
                    val idCurrentUser = UserFirebase.getCurrentUser()!!.uid
                    val like = Like().apply {
                        setIdPost(post.getId())
                        setIdUser(idCurrentUser)
                    }
                    val gestureDetector = GestureDetector(imagePost.context, object : GestureDetector.SimpleOnGestureListener() {
                        override fun onDoubleTap(e: MotionEvent): Boolean {
                            like.save()
                            imagelike.setImageResource(R.drawable.liked)
                            return true
                        }
                    })
                    if(ds.hasChild(idCurrentUser)) {
                        Log.d("log", "${ds}")
                        imagelike.setImageResource(R.drawable.liked)
                        imagelike.setOnClickListener {
                            like.remove()
                        }
                    }
                    else{
                        Log.d("log", "${ds}")
                        imagelike.setImageResource(R.drawable.like)
                        imagelike.setOnClickListener {
                            like.save()
                        }
                        imagePost.setOnTouchListener { _, event ->
                            gestureDetector.onTouchEvent(event)
                            true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            imageComment.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("POST_ID", post.getId())
                context.startActivity(intent)
            }
        }

    }
}
