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
import com.example.apppostagens.Model.Post
import com.example.apppostagens.R
import com.google.firebase.storage.FirebaseStorage


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
            userName.text = post.getUser().getName()
            userNameDescription.text = post.getUser().getName()
            description.text = post.getDescription()
            date.text = post.getDate()

            val postImageReference = FirebaseStorage.getInstance().reference.child(post.getImageUrl())
            println(postImageReference)
            postImageReference.downloadUrl.addOnSuccessListener { uri ->

                Glide.with(itemView.context)
                    .load(uri.toString())
                    .placeholder(R.drawable.image)
                    .error(R.drawable.broken_image)
                    .into(imagePost)

            }.addOnFailureListener {
                Log.e("FirebaseStorage", "Link ${it.message}")
            }

            val userImageReference = FirebaseStorage.getInstance().reference.child(post.getUser().getUserImage())
            println(userImageReference)
            userImageReference.downloadUrl.addOnSuccessListener { uri ->

                Glide.with(itemView.context)
                    .load(uri.toString())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(userImage)

            }.addOnFailureListener {
                Log.e("FirebaseStorage", "Erro ao buscar imagem: ${it.message}")
            }

            val gestureDetector = GestureDetector(imagePost.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    post.setLiked(true)
                    imagelike.setImageResource(R.drawable.liked)
                    return true
                }
            })
            imagePost.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }

            imageSave.setOnClickListener {
                post.setSaved(!post.getSaved())
                imageSave.setImageResource(if (post.getSaved()) R.drawable.saved else R.drawable.save)
            }

            imagelike.setOnClickListener {
                post.setLiked(!post.getLiked())
                imagelike.setImageResource(if (post.getLiked()) R.drawable.liked else R.drawable.like)
            }

            imageComment.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("POST_ID", post.getId())
                context.startActivity(intent)
            }
        }

    }
}
