package com.example.apppostagens.Utils

import android.net.Uri
import android.util.Log
import com.example.apppostagens.Model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

object UserFirebase {

    fun getCurrentUser(): FirebaseUser? {
        val user : FirebaseAuth = FirebaseConfiguration.getFirebaseAuthReference()
        return user.currentUser
    }

    fun updateNameUser(userName : String){
        try {

            val currentUser : FirebaseUser? = getCurrentUser()
            val profile = UserProfileChangeRequest
                .Builder()
                .setDisplayName(userName)
                .build()
            currentUser?.updateProfile(profile)?.addOnCompleteListener(
                OnCompleteListener {task ->
                    if(!task.isSuccessful){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.")
                    }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun updateImageUser(uri : Uri){
        try {

            val currentUser : FirebaseUser? = getCurrentUser()
            val profile = UserProfileChangeRequest
                .Builder()
                .setPhotoUri(uri)
                .build()
            currentUser?.updateProfile(profile)?.addOnCompleteListener(
                OnCompleteListener {task ->
                    if(!task.isSuccessful){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.")
                    }
                })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getDataCurrentUser(): User? {
        val firebaseUser = getCurrentUser() ?: return null

        return User().apply {
            setEmail(firebaseUser.email ?: "")
            setUsername(firebaseUser.displayName ?: "")
            setId(firebaseUser.uid ?: "")
            setUserImage(firebaseUser.photoUrl?.toString() ?: "")
        }
    }
}