package com.example.apppostagens.Adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.apppostagens.Model.Post
import com.example.apppostagens.R

class PostAdapter(private val lista: List<Post>) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = lista[position]

        val image = ContextCompat.getDrawable(holder.itemView.context, post.getImage())
        val userImage = ContextCompat.getDrawable(holder.itemView.context, post.getUserImage().userImage)

        holder.userName.text = post.getName().name
        holder.description.text = post.getDescription()
        holder.imagePost.background = image
        holder.userImage.setImageDrawable(userImage)

        holder.imageSave.setOnClickListener {
            post.setSaved(!post.getSaved())
            holder.imageSave.setImageResource(if (post.getSaved()) R.drawable.saved else R.drawable.save)
        }

        holder.imagelike.setOnClickListener {
            post.setLiked(post.getLiked())
            holder.imagelike.setImageResource(if (post.getLiked()) R.drawable.liked else R.drawable.like)
        }
    }

    override fun getItemCount(): Int = lista.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val description: TextView = itemView.findViewById(R.id.description)
        val imagePost: ImageView = itemView.findViewById(R.id.imagePost)
        val imagelike: ImageView = itemView.findViewById(R.id.imageLike)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val imageSave: ImageView = itemView.findViewById(R.id.imageSave)
        val date: TextView = itemView.findViewById(R.id.date)
    }
}
