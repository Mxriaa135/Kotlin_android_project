package com.example.apppostagens.Model

class Comment() {
    private var post: Post = Post()
    private var user: User = User()
    private var text: String = ""

    constructor(post: Post, user: User, text: String) : this(){
        this.post = post
        this.user = user
        this.text = text
    }

    fun getPost(): Post{
        return post
    }
    fun setPost(post: Post){
        this.post = post
    }

    fun getUser(): User{
        return user
    }
    fun setUser(user: User){
        this.user = user
    }

    fun getText(): String{
        return text
    }

    fun setText(text: String){
        this.text = text
    }
}