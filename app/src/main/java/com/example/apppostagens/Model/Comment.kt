package com.example.apppostagens.Model

class Comment() {
    private var post: Post = Post()
    private var user: User = User()
    private var text: String = ""

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