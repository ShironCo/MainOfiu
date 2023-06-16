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
        // Se obtienen los datos del perfil desde las preferencias y se asignan a las variables correspondientes
        _nameText.value = preferencesManager.getDataProfile(Variables.NameUser.title)
        _emailText.value = preferencesManager.getDataProfile(Variables.EmailUser.title)
        _phoneText.value = preferencesManager.getDataProfile(Variables.PhoneUser.title)
        _imageProfile.value = preferencesManager.getDataProfile(Variables.ImageUser.title).toUri()
    }

    fun onOpenDialog(){
        // Se invierte el valor de la variable _openDialog para mostrar/ocultar un diálogo
        _openDialog.value = _openDialog.value != true
    }

    fun onExpandMenu(){
        // Se invierte el valor de la variable _expandMenu para expandir/contraer un menú
        _expandMenu.value = _expandMenu.value != true
    }

    fun setImageProfile(image: Uri) {
        // Se asigna la imagen de perfil seleccionada a la variable _imageProfile
        _imageProfile.value = image
    }

    // Esta función convierte una Uri en un Bitmap utilizando corutinas para realizar la operación de manera asíncrona
    // Se utiliza el ImageLoader de Android para cargar la imagen desde la Uri y convertirla en un Bitmap
    // El resultado se retorna como un objeto Bitmap
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

    // Esta función convierte una Uri en un array de bytes (ByteArray) utilizando corutinas para realizar la operación de manera asíncrona
    // Primero se obtiene un Bitmap a partir de la Uri utilizando la función bitmapFromUri
    // Luego se comprime el Bitmap en formato JPEG y se guarda en un OutputStream
    // Finalmente, se obtienen los bytes del OutputStream y se retornan como un ByteArray
    private suspend fun uriToBytes(uri: Uri, context: Context): ByteArray {
        return withContext(Dispatchers.IO) {
            val bitmap = bitmapFromUri(uri, context)
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.toByteArray()
        }
    }

    // Esta función obtiene la extensión de un archivo a partir de una Uri utilizando el ContentResolver de Android
    // Si la Uri tiene el esquema "content", se utiliza el ContentResolver para obtener el tipo MIME y luego se obtiene la extensión correspondiente
    // Si la Uri tiene otro esquema, se obtiene la extensión a partir de la ruta
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

    // Esta función actualiza la foto de perfil del usuario
    // Se obtiene el ID del usuario desde las preferencias y se crea un RequestBody a partir del ID
    // Se convierte la Uri en un ByteArray utilizando la función uriToBytes
    // Se obtiene la extensión de la imagen utilizando la función extensionFromUri
    // Se crea un MultipartBody.Part con el ByteArray convertido y la extensión, representando la imagen
    // Luego se realiza la llamada al repositorio para actualizar la foto de perfil
    // Si la operación tiene éxito, se muestra un mensaje de éxito, de lo contrario se muestra un mensaje de error
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


    // Esta función cierra la sesión del usuario
    // Se borran los datos del perfil de las preferencias
    // Se navega de regreso a la pantalla de inicio de sesión
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

    // Esta función navega a la pantalla de cambio de cuenta
    fun onChangeAccount(navController: NavController){
        navController.popBackStack()
        navController.navigate(AppScreens.Menu.route)
    }
}