package com.example.apppostagens.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseConfiguration {
    private val firebaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val firebaseAuthReference: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage: StorageReference = FirebaseStorage.getInstance().reference

    //retorna a referencia do database
    fun getFirebaseReference(): DatabaseReference {
        return firebaseReference
    }

    //retorna a instancia do FirebaseAuth
    fun getFirebaseAuthReference(): FirebaseAuth {
        return firebaseAuthReference
    }

    //Retorna instancia do FirebaseStorage
    fun getFirebaseStorage(): StorageReference {
        return storage
    }
}