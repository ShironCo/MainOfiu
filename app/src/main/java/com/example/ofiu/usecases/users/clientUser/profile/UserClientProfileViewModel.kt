package com.example.ofiu.usecases.users.clientUser.profile

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
import androidx.navigation.NavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.usecases.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserClientProfileViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _emailText = MutableLiveData<String>()
    val emailText : LiveData<String> = _emailText

    private val _nameText = MutableLiveData<String>()
    val nameText : LiveData<String> = _nameText

    private val _phoneText = MutableLiveData<String>()
    val phoneText : LiveData<String> = _phoneText

    private val _expandMenu = MutableLiveData<Boolean>()
    val expandMenu : LiveData<Boolean> = _expandMenu

    private val _openDialog = MutableLiveData<Boolean>()
    val openDialog : LiveData<Boolean> = _openDialog

    private val _imageProfile = MutableLiveData<Uri>()
    val imageProfile: LiveData<Uri> = _imageProfile

    init {
        _nameText.value = preferencesManager.getDataProfile(Variables.NameUser.title)
        _emailText.value = preferencesManager.getDataProfile(Variables.EmailUser.title)
        _phoneText.value = preferencesManager.getDataProfile(Variables.PhoneUser.title)
        _imageProfile.value = preferencesManager.getDataProfile(Variables.ImageUser.title).toUri()
    }

    fun onOpenDialog(){
        _openDialog.value = _openDialog.value != true
    }

    fun onExpandMenu(){
        _expandMenu.value = _expandMenu.value != true
    }

    fun setImageProfile(image: Uri) {
        _imageProfile.value = image
    }
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

    fun onCloseSession(navController: NavController){
        preferencesManager.setDataProfile(Variables.EmailUser.title, "")
        preferencesManager.setDataProfile(Variables.PasswordUser.title, "")
        preferencesManager.setDataProfile(Variables.LoginActive.title, "false")
        preferencesManager.setDataProfile(Variables.DescriptionPro.title, "")
        preferencesManager.setDataProfile(Variables.ImageUser.title, "")
        preferencesManager.setDataProfile(Variables.PhoneUser.title, "")
        preferencesManager.setDataProfile(Variables.EmailUser.title, "")
        navController.popBackStack()
        navController.navigate(AppScreens.Session.route)
    }

    fun onChangeAccount(navController: NavController){
        navController.popBackStack()
        navController.navigate(AppScreens.Menu.route)
    }
}