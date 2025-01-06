package com.example.apppostagens.Model

class User {
    var name: String
    var userImage: Int
    var email: String? = null
    var telefone: String? = null

    constructor(name: String, userImage: Int) {
        this.name = name
        this.userImage = userImage
    }

    constructor(name: String, image: Int, email: String?, telefone: String?) {
        this.name = name
        this.userImage = image
        this.email = email
        this.telefone = telefone
    }
}