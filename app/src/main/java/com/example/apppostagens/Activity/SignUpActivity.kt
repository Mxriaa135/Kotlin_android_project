package com.example.apppostagens.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class SignUpActivity : AppCompatActivity() {

    private lateinit var nameInput : EditText
    private lateinit var usernameInput : EditText
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var buttonSingUp : Button
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeComponents()

        progressBar.setVisibility(View.GONE)
        buttonSingUp.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val name = nameInput.text.toString()
                val username = usernameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                if (!name.isEmpty()){
                    if (!username.isEmpty()){
                        if (!email.isEmpty()){
                            if (!password.isEmpty()){
                                val user = User(name, username, email, password)
                                signUp(user)
                            }
                            else{
                                Toast.makeText(this@SignUpActivity,
                                    "Digite a senha", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this@SignUpActivity,
                                "Digite o email", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@SignUpActivity,
                            "Digite o username", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@SignUpActivity,
                        "Digite seu Nome", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun signUp(user : User){
        progressBar.setVisibility(View.VISIBLE)
        val authenticator = FirebaseConfiguration.getFirebaseAuthReference()
        authenticator.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
            .addOnCompleteListener(this@SignUpActivity) { task ->
                if(task.isSuccessful){
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                }
                else{
                    progressBar.setVisibility(View.GONE)

                    var errorException = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        errorException = "Digite uma senha mais forte!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        errorException = "Por favor, digite um e-mail válido"
                    } catch (e: FirebaseAuthUserCollisionException) {
                        errorException = "Este conta já foi cadastrada"
                    } catch (e: Exception) {
                        errorException = "ao cadastrar usuário: " + e.message
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        this@SignUpActivity,
                        "Erro: $errorException",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun initializeComponents(){
        nameInput = findViewById(R.id.nameSignUpInput)
        usernameInput = findViewById(R.id.usernameSignUpInput)
        emailInput = findViewById(R.id.emailSingUpInput)
        passwordInput = findViewById(R.id.passwordSignUpInput)
        buttonSingUp = findViewById(R.id.buttonSignUp)
        progressBar = findViewById(R.id.progressBarSignUp)

        nameInput.requestFocus()
    }
}