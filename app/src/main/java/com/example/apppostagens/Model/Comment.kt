package com.example.apppostagens.Model

class Comment(
    private var post: Post,
    private var user: User,
    private var name: User,
    private var userImage: User,
    private var text: String)
{

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

    fun getText(): String{
        return text
    }

    fun setText(text: String){
        this.text = text
    }
}