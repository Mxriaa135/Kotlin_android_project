package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apppostagens.Fragments.FeedFragment
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.databinding.ActivityMainBinding
import com.example.apppostagens.databinding.NavigationBottomBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createPost()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_container_Main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnve)
        bottomNavigationView.setupWithNavController(navController)

    }
    fun createPost(){
        // Criando o usuário teste
        var user1 = User("Amelia", R.drawable.black)
        reference.child("User").push().setValue(user1)

        //Criando os posts testes
        var post1 = Post(1,"The beautiful sunset!", R.drawable.sunset, "01-02-2025", user1, user1)
        var post2 = Post(2,"My sweet cat <3", R.drawable.cat, "01-08-2025", user1, user1)
        reference.child("Post").push().setValue(post1)
        reference.child("Post").push().setValue(post2)
    }
}