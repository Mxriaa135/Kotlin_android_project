package com.example.apppostagens.Model

class Post () {
    private var id: Int = 0
    private var description: String = ""
    private var imageUrl: String = ""
    private var date: String = ""
    private var saved: Boolean = false
    private var liked: Boolean = false
    private var user: User = User()
    private var comments: List<Comment> = ArrayList<Comment>()

    constructor(id: Int, description: String, imageUrl: String, date: String, user: User) : this(){
        this.id = id
        this.description = description
        this.imageUrl = imageUrl
        this.date = date
        this.user = user
    }

    fun getId(): Int{
        return id
    }

    fun setId(id:Int){
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

    fun getLiked(): Boolean{
        return liked
    }

    fun setLiked(liked: Boolean){
        this.liked = liked
    }

    fun getDate (): String{
        return date
    }

    fun setDate(date: String){
        this.date = date
    }

    fun getUser(): User {
        return user
    }

    fun setUser(user: User) {
        this.user = user
    }

    fun getComments(): List<Comment> {
        return comments
    }

    fun setComments(comments: List<Comment>) {
        this.comments = comments
    }
}