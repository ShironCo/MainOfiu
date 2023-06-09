package com.example.ofiu.usecases.users.workerUser.profile

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
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

    private val _toggleDesc = MutableLiveData<Boolean>()
    val toggleDesc: LiveData<Boolean> = _toggleDesc

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _desc = MutableLiveData<String>()
    val desc: LiveData<String> = _desc

    private val _descDialog = MutableLiveData<String>()
    val descDialog: LiveData<String> = _descDialog

    private val _descEmpty = MutableLiveData<Boolean>()

    private val _imageGallery = MutableLiveData<List<Uri>>()
    val imageGallery: LiveData<List<Uri>> = _imageGallery

    private val _imageProfile = MutableLiveData<Uri>()
    val imageProfile: LiveData<Uri> = _imageProfile

    private val _response = MutableLiveData<String>()
    val response : LiveData<String> = _response

    init {
        _id.value = preferencesManager.getDataProfile(Variables.IdUser.title)
        _name.value = preferencesManager.getDataProfile(Variables.NameUser.title)
        _email.value = preferencesManager.getDataProfile(Variables.EmailUser.title)
        _phone.value = preferencesManager.getDataProfile(Variables.PhoneUser.title)
        _desc.value = preferencesManager.getDataProfile(Variables.DescriptionPro.title)
    }

    fun setImageProfile(image: Uri){
        _imageProfile.value = image
    }

    fun setImages(image: List<Uri>): List<Uri>{
        _imageGallery.value = image.take(4)
        return image.take(2)
    }

    fun onTextChange(desc: String) {
        _desc.value = desc
        _descEmpty.value = desc.isNotBlank()
    }

    fun onSetToggleDesc(value: Boolean) {
        _toggleDesc.value = value
    }

//    fun onSaveImages(uris: List<Uri>, context: Context) {
//           val valor = "1"
//            preferencesManager.setDataProfile("image1", uris.get(0).toString())
//         preferencesManager.setDataProfile(Variables.ImageGalleryCant.title, uris.size.toString())
//    }

    suspend fun bitmapFromUri(uri: Uri, context: Context): Bitmap?{
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

    private suspend fun uriToBytes(uri: Uri, context: Context): ByteArray {
        return withContext(Dispatchers.IO) {
            val bitmap = bitmapFromUri(uri, context)
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.toByteArray()
        }
    }

    private  fun extensionFromUri(uri: Uri, context:Context): String? {
        return if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            uri.path?.let {
                MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString())
            }
        }
    }

    private suspend fun locationToMultipart(photos: List<Uri>, context: Context): List<MultipartBody.Part> {
        return photos.mapIndexed { index, uri ->
            val uriBytes = uriToBytes(uri, context)
            MultipartBody.Part.createFormData(
                "photo$index", //Yo le pongo un nombre a cada foto para identificarla. Fijate que es lo que te pide la API
                "image/jpeg",
                uriBytes.toRequestBody(contentType = "image/jpeg".toMediaTypeOrNull())
            )
        }
    }

    fun onSendImage(context: Context){
        viewModelScope.launch {
            val uri = _imageGallery.value!!
            if (uri.isNotEmpty()){
                val id = _id.value!!
                val requestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
                val list : List<MultipartBody.Part> = locationToMultipart(uri, context)
                val array = list.toTypedArray()
                repository.sendImageGallery(requestBody,array) .onSuccess {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                }.onFailure {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun onGenerateDesc(context: Context){
        if (_descDialog.value.isNullOrBlank()){
            _isLoading.value = true
            viewModelScope.launch {
                repository.promptGpt(_descDialog.value!!).onSuccess {
                    _descDialog.value = it.choices.first().text.trimStart('\n').trimStart('\n')
                }.onFailure {
                    _response.value = it.message
                }
                _isLoading.value = false
            }
        }else{
            _descDialog.value = ""
        }
    }

    fun onSaveDesc() {
        if (_desc.value.isNullOrBlank()) {
        } else {
            preferencesManager.setDataProfile(Variables.DescriptionPro.title, _desc.value!!)
        }
    }
}