package com.example.ofiu.usecases.users.workerUser.profile

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.gpt.UserProRequest
import com.example.ofiu.remote.dto.ofiu.UserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _id = MutableLiveData<String>()
    val id: LiveData<String> = _id

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _comments = MutableLiveData<String>()
    val comments: LiveData<String> = _comments

    private val _starts = MutableLiveData<String>()
    val starts: LiveData<String> = _starts

    private val _toggleDesc = MutableLiveData<Boolean>()
    val toggleDesc: LiveData<Boolean> = _toggleDesc

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingDesc = MutableLiveData<Boolean>()
    val isLoadingDesc: LiveData<Boolean> = _isLoadingDesc

    private val _imagePreviewToggle = MutableLiveData<Boolean>()
    val imagePreviewToggle: LiveData<Boolean> = _imagePreviewToggle

    private val _imagePreview = MutableLiveData<Uri>()
    val imagePreview: LiveData<Uri> = _imagePreview

    private val _desc = MutableLiveData<String>()
    val desc: LiveData<String> = _desc

    private val _descDialog = MutableLiveData<String>()
    val descDialog: LiveData<String> = _descDialog

    private val _descEmpty = MutableLiveData<Boolean>()
    val descEmpty: LiveData<Boolean> = _descEmpty

    private val _imageGallery = MutableLiveData<List<Uri>>()
    val imageGallery: LiveData<List<Uri>> = _imageGallery

    private val _imageProfile = MutableLiveData<Uri>()
    val imageProfile: LiveData<Uri> = _imageProfile

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    init {
        _id.value = preferencesManager.getDataProfile(Variables.IdUser.title)
        _name.value = preferencesManager.getDataProfile(Variables.NameUser.title)
        _email.value = preferencesManager.getDataProfile(Variables.EmailUser.title)
        _phone.value = preferencesManager.getDataProfile(Variables.PhoneUser.title)
        _desc.value = preferencesManager.getDataProfile(Variables.DescriptionPro.title)

        if (_imageGallery.value.isNullOrEmpty()) {
            viewModelScope.launch {
                repository.receiveImagesPro(UserProRequest(_id.value!!)).onSuccess {
                    _desc.value = it.data.desc.substringBeforeLast("-")
                    _imageProfile.value = it.data.image.toUri()
                    _comments.value = it.data.comentarios.toString()
                    _starts.value = it.data.estrellas.toString()
                    val list = mutableListOf<Uri>()
                    it.data1.forEach { uris ->
                        list.add(uris.toUri())
                    }
                    _imageGallery.value = list
                }.onFailure {
                    println(it)
                }
            }
        }
    }

    fun setImagePreviewToggle() {
        _imagePreviewToggle.value = _imagePreviewToggle.value != true
    }

    fun setImagePreview(value: Uri) {
        _imagePreview.value = value
        setImagePreviewToggle()
    }

    fun setImageProfile(image: Uri) {
        _imageProfile.value = image
    }

    fun setImages(image: List<Uri>) {
        if (_imageGallery.value.isNullOrEmpty()) {
            _imageGallery.value = image
        } else {
            val list = mutableListOf<Uri>()
            _imageGallery.value!!.forEach {
                list.add(it)
            }
            image.forEach {
                list.add(it)
            }
            _imageGallery.value = list
        }
    }

    fun onTextChange(desc: String, descDialog: String) {
        _desc.value = desc
        _descDialog.value = descDialog
        _descEmpty.value = descDialog.isNotBlank()
    }

    fun onSetToggleDesc(value: Boolean) {
        _toggleDesc.value = value
    }


    //De Uri a bitmap
    suspend fun bitmapFromUri(uri: Uri, context: Context): Bitmap? {
        return withContext(Dispatchers.IO) {
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(uri)
                .build()
            val result = (imageLoader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            bitmap
        }
    }

    //Convertor de URI a bytes
    private suspend fun uriToBytes(uri: Uri, context: Context): ByteArray {
        return withContext(Dispatchers.IO) {
            val bitmap = bitmapFromUri(uri, context)
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.toByteArray()
        }
    }

    //Funcion para identificar la extension
    private fun extensionFromUri(uri: Uri, context: Context): String? {
        return if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            uri.path?.let {
                MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString())
            }
        }
    }

    private suspend fun locationToMultipart(
        photos: List<Uri>,
        context: Context
    ): List<MultipartBody.Part> {
        return photos.mapIndexed { index, uri ->
            val extension = extensionFromUri(uri, context)
            val uriBytes = uriToBytes(uri, context)
            MultipartBody.Part.createFormData(
                "photo$index", //Yo le pongo un nombre a cada foto para identificarla. Fijate que es lo que te pide la API
//                "image/jpeg",
                filename = "photo$index.$extension", //Nombre de la imagen mas su extension
                uriBytes.toRequestBody(contentType = "image/jpeg".toMediaTypeOrNull()) //El requestBody
            )
        }
    }

    fun onSendImage(context: Context, uri: List<Uri>) {
        _isLoading.value = true
        viewModelScope.launch {
            if (uri.isNotEmpty()) {
                val id = _id.value!!
                val requestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
                val list: List<MultipartBody.Part> = locationToMultipart(uri, context)
                val array = list.toTypedArray()
                repository.sendImageGallery(requestBody, array).onSuccess {
                    // Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }.onFailure {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
            _isLoading.value = false
        }
    }

    fun onGenerateDesc(context: Context) {
        if (_descDialog.value.isNullOrBlank()) {
            //  _descDialog.value = ""
        } else {
            _isLoadingDesc.value = true
            viewModelScope.launch {
                repository.promptGpt(_descDialog.value!!).onSuccess {
                    _descDialog.value = it.choices.first().text.trimStart('\n').trimStart('\n')
                }.onFailure {
                    _response.value = it.message
                }
                _isLoadingDesc.value = false
            }
        }
    }

    private suspend fun onSaveDescAndTags(context: Context) {
        withContext(Dispatchers.IO) {
            var tags: String? = null
            val id = preferencesManager.getDataProfile(Variables.IdUser.title, "")
            repository.getTags(_descDialog.value!!).onSuccess {
                tags = it.choices.first().text.trimStart('\n').trimStart('\n')
                println(tags)
            }.onFailure {
                tags = "No hay etiqueta"
            }
            repository.sendDescrPro(UserRequest(id, "${_descDialog.value!!}-${tags}")).onSuccess {
                if (it.response == "true") {
                    preferencesManager.setDataProfile(
                        Variables.DescriptionPro.title,
                        _descDialog.value!!
                    )
                    _desc.postValue(_descDialog.value)
                    _toggleDesc.postValue(false)
                }
            }.onFailure {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
            }
        }
    }

     fun onClickButtonSave(context: Context){
         _isLoadingDesc.value = true
         viewModelScope.launch {
             onSaveDescAndTags(context)
             _isLoadingDesc.value = false
         }
    }

    fun onDeleteImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            repository.deleteImageGallery("..${uri.path}").onSuccess {
                if (it.response == "true") {
                    val currentList = _imageGallery.value?.toMutableList()
                    currentList?.remove(uri)
                    _imageGallery.postValue(currentList)
                } else {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }
            }.onFailure {
                Toast.makeText(context, "Ha ocurrido un error ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun updatePhotoProfile(uri: Uri, context: Context) {
        viewModelScope.launch {
            val id = preferencesManager.getDataProfile(Variables.IdUser.title, "")
            val requestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
            val uriToBytes = uriToBytes(uri, context)
            val extension = extensionFromUri(uri, context)
            val image = MultipartBody.Part.createFormData(
                name = "photo",
                filename = "photo.$extension",
                uriToBytes.toRequestBody(contentType = "image/jpeg".toMediaTypeOrNull())
            )
            repository.updatePhotoProfile(requestBody, image).onSuccess {
                Toast.makeText(context, it.response, Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                println(it)
            }
        }
    }
}