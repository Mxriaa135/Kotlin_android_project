package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppostagens.Adapter.CommentAdapter
import com.example.apppostagens.Fragments.CommentFragment
import com.example.apppostagens.R
import com.example.apppostagens.databinding.ActivityCommentBinding
import com.example.apppostagens.databinding.ActivityMainBinding
import org.w3c.dom.Comment

class CommentActivity : AppCompatActivity() {

    lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getStringExtra("POST_ID")

        val fragment = CommentFragment().apply {
            arguments = Bundle().apply {
                putString("POST_ID", postId)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerComment, fragment)
            .commit()

    }

}