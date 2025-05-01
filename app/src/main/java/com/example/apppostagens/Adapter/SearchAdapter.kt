package com.example.apppostagens.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.databinding.SearchUserBinding

class SearchAdapter(private val list: List<User>, private val listener: OnUserClickListener)
    : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(val binding: SearchUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = list[position]

        Glide.with(holder.itemView.context)
            .load(user.getUserImage())
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(holder.binding.userImage)

        holder.binding.userNameText.text = user.getUsername()
        holder.binding.nameText.text = user.getName()

        holder.itemView.setOnClickListener {
            listener.onUserClick(user.getId())
        }

    }
    override fun getItemCount(): Int = list.size

    interface OnUserClickListener {
        fun onUserClick(userId: String)
    }
}