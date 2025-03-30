package com.example.apppostagens.Utils

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

object UserFirebase {
    fun getCurrentUser(): FirebaseUser? {
        val user : FirebaseAuth = FirebaseConfiguration.getFirebaseAuthReference()
        return user.currentUser
    }
}