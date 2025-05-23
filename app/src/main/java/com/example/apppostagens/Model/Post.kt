package com.example.apppostagens.Model

import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.firebase.database.DatabaseReference

class Post () {
    private var id: String = ""
    private var description: String = ""
    private var imageUrl: String = ""
    private var date: String = ""
    private var saved: Boolean = false
    private var userId: String = ""

    fun add() {
        val firebasref : DatabaseReference = FirebaseConfiguration.getFirebaseReference()
        val postRef : DatabaseReference = firebasref.child("Post")
        val idPostagem: String = postRef.push().getKey()!!
        setId(idPostagem)
    }

    fun save() : Boolean{
        val firebaseRef: DatabaseReference = FirebaseConfiguration.getFirebaseReference()
        val postagensRef = firebaseRef.child("Post")
            .child(getUserId())
            .child(getId())
            .setValue(this)
        val feedRef = firebaseRef.child("Feed")
            .child(getId())
            .setValue(this)
        return true
    }

    constructor(id: String, description: String, imageUrl: String, date: String, userId: String) : this(){
        this.id = id
        this.description = description
        this.imageUrl = imageUrl
        this.date = date
        this.userId = userId
    }

    fun getId(): String{
        return id
    }

    fun setId(id:String){
        this.id = id
    }

    fun getDescription(): String{
        return description
    }

    fun setDescription(description: String){
        this.description = description
    }

    fun getSaved(): Boolean{
        return saved
    }

    fun setSaved(saved: Boolean){
        this.saved = saved
    }

    fun getImageUrl(): String{
        return imageUrl
    }

    fun setImageUrl(image: String){
        this.imageUrl = image
    }

    fun getDate (): String{
        return date
    }

    fun setDate(date: String){
        this.date = date
    }

    fun getUserId(): String {
        return userId
    }

    fun setUserId(user: String) {
        this.userId = user
    }
}