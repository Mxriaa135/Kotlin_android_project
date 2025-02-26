package com.example.apppostagens.Model

class User() {
    var name: String = ""
    var userImage: String = ""
    var email: String? = null
    var telefone: String? = null

    constructor(name: String, userImage: String) : this(){
        this.name = name
        this.userImage = userImage
    }

    constructor(name: String, image: String, email: String?, telefone: String?) : this(){
        this.name = name
        this.userImage = image
        this.email = email
        this.telefone = telefone
    }
}