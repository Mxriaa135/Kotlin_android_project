package com.example.apppostagens.Adapter

import android.content.Context
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apppostagens.Adapter.PostAdapter.MyViewHolder
import com.example.apppostagens.Model.Post
import com.example.apppostagens.R
import com.example.apppostagens.databinding.GridPostBinding
import com.example.apppostagens.databinding.SearchUserBinding

class GridAdapter(
    context: Context,
    private val resource: Int,
    private val items: List<String>
) : ArrayAdapter<String>(context, resource, items) {
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: GridPostBinding
        val view: View

        if (convertView == null) {
            binding = GridPostBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as GridPostBinding
        }

        val urlImagem = getItem(position)

        binding.progressBar.visibility = View.VISIBLE

        Glide.with(context)
            .load(urlImagem)
            .placeholder(R.drawable.image)
            .error(R.drawable.broken_image)
            .centerCrop()
            .into(binding.imagePost)

        binding.progressBar.visibility = View.GONE

        return view
    }
}
