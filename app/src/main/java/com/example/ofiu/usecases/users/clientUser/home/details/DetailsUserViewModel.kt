package com.example.ofiu.usecases.users.clientUser.home.details

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.Comentario
import com.example.ofiu.usecases.navigation.AppScreens
import com.google.errorprone.annotations.Var
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsUserViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val db = Firebase.firestore

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _id = MutableLiveData<String>()

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _imageProfile = MutableLiveData<Uri>()
    val imageProfile : LiveData<Uri> = _imageProfile

    private val _starts = MutableLiveData<String>()
    val starts : LiveData<String> = _starts

    private val _desc = MutableLiveData<String>()
    val desc : LiveData<String> = _desc

    private val _cantComments = MutableLiveData<String>()
    val cantComments : LiveData<String> = _cantComments

    private val _imageGallery = MutableLiveData<List<Uri?>>()
    val imageGallery : LiveData<List<Uri?>> = _imageGallery

    private val _expandMenu = MutableLiveData<Boolean>()
    val expandMenu : LiveData <Boolean> = _expandMenu

    private val _imagePreview = MutableLiveData<Uri>()
    val imagePreview : LiveData <Uri> = _imagePreview

    private val _rating = MutableLiveData<Int>()
    val rating : LiveData <Int> = _rating

    private val _comments = MutableLiveData<List<Comentario>>()
    val comments : LiveData <List<Comentario>> = _comments

    private val _commentToggle = MutableLiveData<Boolean>()
    val commentToggle : LiveData <Boolean> = _commentToggle

    private val _opinion = MutableLiveData<String>()
    val opinion : LiveData <String> = _opinion

    private val _publicar = MutableLiveData<Boolean>()
    val publicar : LiveData <Boolean> = _publicar

    fun onTextChange(opinion: String){
        _opinion.value = opinion
    }

    fun changeToggleComment(){
        _commentToggle.value = _commentToggle.value != true
    }

    fun setId(id: String){
        _id.value = id
    }

    fun setRating(value: Int){
        _rating.value = value
    }

    fun setData(value: Boolean, imagePreview: Uri){
        _expandMenu.value = value
        _imagePreview.value = imagePreview
    }
    fun getDatUser(id: String){
        viewModelScope.launch {
            repository.getDetailsPro(id).onSuccess {
                it.let {
                    _imageProfile.value = it.informacion.imgPerfil.toUri()
                    _name.value = it.informacion.nombre
                    _cantComments.value = it.informacion.comentarios
                    _desc.value = it.informacion.descProfesional
                    _starts.value = it.informacion.estrellas
                    val images = mutableListOf<Uri>()
                    it.imagenes.forEach{uri->
                        images.add(uri.toUri())
                    }
                    _imageGallery.value = images
                }
            }.onFailure {
                println("error $it")
            }
            getCommentsUser(id)
            _isLoading.value = false
        }
    }
    private suspend fun  getCommentsUser(id: String){
        repository.getCommentsPro(id = id).onSuccess {
            println(it)
            val comments = mutableListOf<Comentario>()
            it.let {
                it.comentarios.forEach{ comment ->
                    comments.add(comment)
                }
                _comments.postValue(comments)
            }
        }.onFailure {
            println("Error en comentarios $it")
        }
    }

    fun onClickPublicar(rating: Int, idPro:String, desc:String) {
        viewModelScope.launch {
            val idUser = preferencesManager.getDataProfile(Variables.IdUser.title)
            repository.setCommentsPro(
                idPro = idPro,
                idUser = idUser,
                desc = desc,
                starts = rating.toString()
            ).onSuccess {
                if (it.response == "true"){_commentToggle.postValue(false)}
            }.onFailure {
                println("error en el envio $it")
            }
        }
    }

    fun onClickChat(navHostController: NavHostController){
        val idUser = preferencesManager.getDataProfile(Variables.IdUser.title)
        val idPro = _id.value
        val name = _name.value
        val imageProfile: String = _imageProfile.value.toString()
        val chatUser = hashMapOf(
            "imageProfile" to imageProfile,
            "name" to name,
            "previewMessage" to "" ,
            "lastMinute" to FieldValue.serverTimestamp(),
            "id" to idPro
        )
        idPro?.let {
            db.collection(idUser).document(it).set(chatUser)
                .addOnSuccessListener {
                }.addOnFailureListener {
                    println("Error al enviar el mensaje: $it")
                }
        }
        navHostController.navigate(AppScreens.Chat.route+"/true"){
        }
    }
}