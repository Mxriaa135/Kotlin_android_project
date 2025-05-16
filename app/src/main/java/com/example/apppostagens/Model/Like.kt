package com.example.apppostagens.Model

import com.example.apppostagens.Utils.FirebaseConfiguration

class Like() {
    private var idUser: String = ""
    private var idPost: String = ""

    fun save() : Boolean{
        FirebaseConfiguration.getFirebaseReference()
            .child("Likes")
            .child(getIdPost())
            .child(getIdUser())
            .setValue(true)

        return true
    }

    fun remove(): Boolean{
        FirebaseConfiguration.getFirebaseReference()
            .child("Likes")
            .child(getIdPost())
            .child(getIdUser())
            .removeValue()

        return true
    }

    constructor(idUser: String, idPost: String): this(){
        this.idUser = idUser
        this.idPost = idPost
    }

    fun getIdUser(): String {
        return idUser
    }

    fun setIdUser(user: String) {
        this.idUser = user
    }

    fun getIdPost(): String {
        return idPost
    }

    fun setIdPost(post: String) {
        this.idPost = post
    }
}