package com.example.apppostagens.Model

import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.firebase.database.DatabaseReference

class Comment() {
    private var id: String = ""
    private var idPost: String = ""
    private var idUser: String = ""
    private var text: String = ""

    fun add() {
        val firebasref : DatabaseReference = FirebaseConfiguration.getFirebaseReference()
        val commentRef : DatabaseReference = firebasref.child("Comments")
        val idComment: String = commentRef.push().getKey()!!
        setId(idComment)
    }

    fun save() : Boolean{
        val firebaseRef: DatabaseReference = FirebaseConfiguration.getFirebaseReference()
        val commentsRef = firebaseRef.child("Comments")
            .child(getIdPost())
            .child(getId())
        commentsRef.setValue(this)
        return true
    }

    fun toMap(): Map<String, Any?>{
        return mapOf(
            "id" to getId(),
            "idPost" to getIdPost(),
            "idUser" to getIdUser(),
            "text" to getText()
        )
    }

    constructor(post: String, user: String, text: String) : this(){
        this.idPost = post
        this.idUser = user
        this.text = text
    }

    fun getId(): String{
        return id
    }
    fun setId(id: String){
        this.id = id
    }

    fun getIdPost(): String{
        return idPost
    }
    fun setIdPost(post: String){
        this.idPost = post
    }

    fun getIdUser(): String{
        return idUser
    }
    fun setIdUser(user: String){
        this.idUser = user
    }
    fun getText(): String{
        return text
    }

    fun setText(text: String){
        this.text = text
    }
}