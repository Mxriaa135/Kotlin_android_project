package com.example.apppostagens.Adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppostagens.Model.Comment
import com.example.apppostagens.R

class CommentAdapter(private val list: List<Comment>) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = list[position]

        Glide.with(holder.itemView.context)
            .load(comment.getUser().getUserImage())
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(holder.userImage)

        holder.userName.text = comment.getUser().getUserImage()
        holder.text.text = comment.getText()
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userNameComment)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val text : TextView = itemView.findViewById(R.id.textComment)
    }
}
