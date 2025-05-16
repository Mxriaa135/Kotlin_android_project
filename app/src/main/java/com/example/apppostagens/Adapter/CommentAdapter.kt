package com.example.apppostagens.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppostagens.Model.Comment
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.example.apppostagens.databinding.CommentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CommentAdapter(private val list: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    inner class CommentHolder(val binding: CommentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = CommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comment = list[position]


        val userRef = FirebaseConfiguration.getFirebaseReference().child("User").child(comment.getIdUser())
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    holder.binding.userNameComment.text = user.getUsername()

                    Glide.with(holder.itemView.context)
                        .load(user.getUserImage())
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(holder.binding.userImage)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        holder.binding.textComment.text = comment.getText()

    }

    override fun getItemCount(): Int = list.size

}
