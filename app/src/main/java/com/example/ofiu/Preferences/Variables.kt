package com.example.ofiu.Preferences

import android.net.Uri

sealed class Variables(
    val title: String
){
    object ImageFrontal : Variables("imageFrontal")
    object ImageTrasera : Variables("imageTrasera")
    object ImageFace : Variables("imageFace")
    object IdUser : Variables("id")
    object IdPro : Variables("idPro")
    object NameUser : Variables("name")
    object EmailUser : Variables("email")
    object PasswordUser : Variables("password")
    object PhoneUser : Variables("phone")
    object LoginActive : Variables("active")
    object Verify : Variables("verify")
    object DescriptionPro : Variables("descripcion")
    object ImageProfile : Variables("imgProfilePro")
}
