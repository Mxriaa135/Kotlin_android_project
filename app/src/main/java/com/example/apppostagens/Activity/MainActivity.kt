package com.example.apppostagens.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.apppostagens.Model.Post
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //createPost()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_container_Main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnve)
        bottomNavigationView.setupWithNavController(navController)

    }
    //Função teste
    /*fun createPost(){

        // Criando o usuário teste
        var user1 = User("Amelia", "imageUserTest.jpeg")
        reference.child("User").push().setValue(user1)

        //Criando os posts testes
        var post1 = Post(1,"The beautiful sunset!", "sunset.jpeg", "01-02-2025", user1)
        var post2 = Post(2,"My sweet cat <3", "cat.jpeg", "01-08-2025", user1)
        reference.child("Post").push().setValue(post1)
        reference.child("Post").push().setValue(post2)
    }*/
}