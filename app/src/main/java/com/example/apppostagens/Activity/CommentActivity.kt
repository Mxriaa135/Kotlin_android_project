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
import org.w3c.dom.Comment

class CommentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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