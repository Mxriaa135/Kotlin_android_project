package com.example.apppostagens.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apppostagens.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SingUpActivity : AppCompatActivity() {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseStorage : StorageReference = FirebaseStorage.getInstance().reference.child("User")
    private lateinit var nameSingUpInput : EditText
    private lateinit var usernameSingUpInput : EditText
    private lateinit var emailSingUpInput : EditText
    private lateinit var passwordSingUpInput : EditText
    private lateinit var buttonSingUp : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sing_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeComponents()

        buttonSingUp.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                var name = nameSingUpInput.text.toString()
                var username = usernameSingUpInput.text.toString()
                var email = emailSingUpInput.text.toString()
                var password = passwordSingUpInput.text.toString()

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SingUpActivity) { task ->
                        if(task.isSuccessful){
                            val intent = Intent(this@SingUpActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{

                        }
                    }
            }
        })

    }

    private fun initializeComponents(){
        nameSingUpInput = findViewById(R.id.nameSingUpInput)
        usernameSingUpInput = findViewById(R.id.usernameSingUpInput)
        emailSingUpInput = findViewById(R.id.emailSingUpInput)
        passwordSingUpInput = findViewById(R.id.passwordSingUpInput)
        buttonSingUp = findViewById(R.id.buttonSingUp)
    }
}