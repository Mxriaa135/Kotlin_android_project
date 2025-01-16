package com.example.apppostagens.Model

class Post (
    private var id: Int,
    private var description: String,
    private var image: Int,
    private var date: String,
    name: User,
    userImage: User
) {
    private var saved: Boolean = false
    private var liked: Boolean = false
    var user: User? = null
    private var name: User
    private var userImage: User
    private var comments: List<Comment>

    init {
        this.name = name
        this.userImage = userImage
        this.comments = ArrayList<Comment>()
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

    fun getImage(): Int{
        return image
    }

    fun setImage(image: Int){
        this.image = image
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

    fun getName(): User {
        return name
    }

    fun setName(name: User) {
        this.name = name
    }

    fun getUserImage(): User {
        return userImage
    }

    fun setUserImage(userImage: User) {
        this.userImage = userImage
    }

    fun getComments(): List<Comment> {
        return comments
    }

    fun setComments(comments: List<Comment>) {
        this.comments = comments
    }
}