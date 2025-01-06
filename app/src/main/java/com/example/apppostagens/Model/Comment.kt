package com.example.apppostagens.Model

class Comment(var user: User, var name: User, var userImage: User, var texto: String) {
    var post: Post? = null
}