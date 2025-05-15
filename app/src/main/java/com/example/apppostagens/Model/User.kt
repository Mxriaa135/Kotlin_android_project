package com.example.apppostagens.Model

import com.example.apppostagens.Utils.FirebaseConfiguration
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude

class User() {
    private var id : String = ""
    private var name: String = ""
    private var username: String = ""
    private var userImage: String = ""
    private var email: String = ""
    private var password : String = ""
    private var posts : Int = 0
    private var followers : Int = 0
    private var following : Int = 0

    fun save(){
        val firebasref : DatabaseReference = FirebaseConfiguration.getFirebaseReference()
        val userRef : DatabaseReference = firebasref.child("User").child(getId())
        userRef.setValue(this)
    }

    fun update(){
        val firebasref : DatabaseReference = FirebaseConfiguration.getFirebaseReference()
        val userRef : DatabaseReference = firebasref.child("User").child(getId())
        userRef.updateChildren(toMap().filterValues { it != null })
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to getEmail(),
            "name" to getName(),
            "id" to getId(),
            "username" to getUsername(),
            "userImage" to getUserImage(),
            "posts" to getPosts(),
            "followers" to getFollowers(),
            "following" to getFollowing()
        )
    }

    constructor(email: String, password: String) : this(){
        this.email = email
        this.password = password
    }

    constructor(name: String, username: String, email: String, password : String) : this(){
        this.name = name
        this.username = username
        this.email = email
        this.password = password
    }

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username.lowercase()
    }

    fun getUserImage(): String {
        return userImage
    }

    fun setUserImage(userImage: String) {
        this.userImage = userImage
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getPosts():Int{
        return posts
    }

    fun setPosts(posts : Int){
        this.posts = posts
    }

    fun getFollowers():Int{
        return followers
    }

    fun setFollowers(followers : Int){
        this.followers = followers
    }

    fun getFollowing():Int{
        return following
    }

    fun setFollowing(following : Int){
        this.following = following
    }

    @Exclude
    fun getPassword(): String {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }
}