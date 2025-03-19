package com.example.apppostagens.Activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apppostagens.Model.User
import com.example.apppostagens.R
import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class LoginActivity : AppCompatActivity() {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var textSignUp : TextView
    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var buttonLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //checkUserAuthentication()
        initializeComponents()

        progressBar.setVisibility(View.GONE)
        buttonLogin.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var email = emailInput.text.toString()
                var password = passwordInput.text.toString()

                if(!email.isEmpty()){
                    if(!password.isEmpty()){
                        var user = User(email, password)
                        signIn(user)
                    }
                    else{
                        Toast.makeText(this@LoginActivity,
                            "Digite a senha", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@LoginActivity,
                        "Digite o email", Toast.LENGTH_SHORT).show()
                }
            }
        })

        textSignUp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View){
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        })

    }
    private fun checkUserAuthentication(){
        val authenticator = FirebaseConfiguration.getFirebaseAuthReference()
        if(authenticator.getCurrentUser() != null ){
            Log.i("CreateUser", "Usuario logado!" );
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun signIn(user: User){
        progressBar.setVisibility(View.VISIBLE)
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    progressBar.setVisibility(View.GONE)

                    var errorException = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        errorException = "Senha inválida!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        errorException = "Por favor, digite um e-mail válido"
                    } catch (e: Exception) {
                        errorException = "ao cadastrar usuário: " + e.message
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        "Erro: $errorException",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    private fun initializeComponents(){
        emailInput = findViewById(R.id.emailLoginInput)
        passwordInput = findViewById(R.id.passwordLoginInput)
        buttonLogin = findViewById(R.id.buttonLogin)
        textSignUp = findViewById(R.id.textLoginSignUp)
        progressBar = findViewById(R.id.progressBarLogin)

        emailInput.requestFocus()
    }
}