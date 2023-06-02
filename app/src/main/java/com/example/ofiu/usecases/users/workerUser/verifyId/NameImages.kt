package com.example.ofiu.usecases.users.workerUser.verifyId

sealed class NameImages(
    val image: String
){
    object ImageFrontal : NameImages("imageFrontal")
    object ImageTrasera : NameImages("imageTrasera")
    object ImageFace : NameImages("imageFace")
}