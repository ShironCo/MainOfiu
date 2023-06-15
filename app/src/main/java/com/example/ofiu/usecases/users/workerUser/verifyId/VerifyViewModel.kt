package com.example.ofiu.usecases.users.workerUser.verifyId

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.domain.OfiuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _sDK28 = MutableLiveData<Boolean>()
    val sDK28: LiveData<Boolean> = _sDK28

    private val _changeView = MutableLiveData<Boolean>()
    val changeView: LiveData<Boolean> = _changeView

    private val _showAlertDialog = MutableLiveData<Int>()
    val showAlertDialog: LiveData<Int> = _showAlertDialog

    private val _nameImage = MutableLiveData<String>()

    private val _previewImage = MutableLiveData<ByteArray>()
    val previewView: LiveData<ByteArray> = _previewImage

    private val _validButtonFace = MutableLiveData<Boolean>()
    val validButtonFace: LiveData<Boolean> = _validButtonFace

    private val _validButtonId = MutableLiveData<Boolean>()
    val validButtonId: LiveData<Boolean> = _validButtonId


    fun verifySuccessful(): Boolean {
        val verify = preferencesManager.getDataProfile(Variables.Verify.title)
        return (verify == "verificado")
    }

    fun setSDK(value: Boolean) {
        _sDK28.value = value
    }

    fun onTextChange(name: String?, alertDialog: Int) {
        _nameImage.value = name
        _showAlertDialog.value = alertDialog
    }

    fun onChangeView() {
        _changeView.value = _changeView.value != true
    }

    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        cameraFacing: Boolean
    ) {
        viewModelScope.launch {
            repository.showCameraPreview(previewView, lifecycleOwner, cameraFacing)
        }
    }

    fun onCleanPreviewImage() {
        _previewImage.value = ByteArray(0)
    }

    fun capture(context: Context) {
        viewModelScope.launch {
            repository.captureImage().takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)
                        image.close()
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.Main) {
                                _previewImage.value = bytes
                            }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("TAG", "Image capture error", exception)
                    }
                }
            )
        }
    }

    fun onValidButtonId() {
        val image1: String = preferencesManager.getDataProfile(Variables.ImageFrontal.title)
        val image2: String = preferencesManager.getDataProfile(Variables.ImageTrasera.title)
        if (image1.isNotBlank()) {
            _validButtonId.value = false
            _validButtonFace.value = true
        }
        if (image2.isNotBlank()) {
            _validButtonFace.value = false
        }
    }

    fun onValidButtonFace() {
        val image1: String = preferencesManager.getDataProfile(Variables.ImageFrontal.title)
        val image2: String = preferencesManager.getDataProfile(Variables.ImageTrasera.title)
        val image3: String = preferencesManager.getDataProfile(Variables.ImageFace.title)
        if (image1.isNotBlank() && image2.isNotBlank()) {
            _validButtonId.value = false
            _validButtonFace.value = image3.isBlank()
        } else {
            _validButtonId.value = true
            _validButtonFace.value = image3.isNotBlank()
        }
    }

    fun onSaveImage(context: Context, navController: NavHostController) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmapImage = BitmapFactory.decodeByteArray(
                _previewImage.value,
                0,
                _previewImage.value!!.size,
                null
            )
            val name = SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.ENGLISH
            ).format(System.currentTimeMillis()) + ".jpg"
            context.openFileOutput(name, Context.MODE_PRIVATE).use { output ->
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
            withContext(Dispatchers.Main) {
                val filePath = File(context.filesDir, name).absolutePath
                preferencesManager.setDataProfile(_nameImage.value.toString(), filePath)
                onCleanPreviewImage()
                onChangeView()
                if (_nameImage.value!! == Variables.ImageTrasera.title) {
                    navController.popBackStack()
                } else if (_nameImage.value!! == Variables.ImageFace.title) {
                    navController.popBackStack()
                    _validButtonFace.value = false
                }
            }
        }
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

    private suspend fun locationToMultipart(
        photos: List<Uri>,
        context: Context
    ): List<MultipartBody.Part> {
        return photos.mapIndexed { index, uri ->
            val uriBytes = uriToBytes(uri, context)
            MultipartBody.Part.createFormData(
                "photo$index", //Yo le pongo un nombre a cada foto para identificarla. Fijate que es lo que te pide la API
//                "image/jpeg",
                filename = "photos", //Nombre de la imagen mas su extension
                uriBytes.toRequestBody(contentType = "image/jpeg".toMediaTypeOrNull()) //El requestBody
            )
        }
    }

    fun onSendImages(navController: NavHostController, context: Context) {
        _validButtonId.value = true
        val uri : List<Uri> = listOf(
            preferencesManager.getDataProfile(Variables.ImageFrontal.title).toUri(),
            preferencesManager.getDataProfile(Variables.ImageTrasera.title).toUri(),
            preferencesManager.getDataProfile(Variables.ImageFace.title).toUri(),
        )
        viewModelScope.launch {
                val id = preferencesManager.getDataProfile(Variables.IdUser.title)
                val requestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
                val list: List<MultipartBody.Part> = locationToMultipart(uri, context)
                val array = list.toTypedArray()
                repository.sendImage(requestBody, array).onSuccess {
                    if (it.response == "Fotos subidas" ){
                        onCleanImages()
                        _showAlertDialog.value = 1
                        delay(4000)
                        navController.popBackStack()
                        println(it.response)
                    }else if (it.response == "Tu verificación ya se encuentra en proceso."){
                        Toast.makeText(context, it.response, Toast.LENGTH_LONG).show()
                        onCleanImages()
                        navController.popBackStack()
                        println(it.response)
                    }
                }.onFailure {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun onCleanImages() {
        deleteImage(Variables.ImageFrontal.title)
        preferencesManager.setDataProfile(Variables.ImageFrontal.title, "")
        deleteImage(Variables.ImageTrasera.title)
        preferencesManager.setDataProfile(Variables.ImageTrasera.title, "")
        deleteImage(Variables.ImageFace.title)
        preferencesManager.setDataProfile(Variables.ImageFace.title, "")
    }


    private fun deleteImage(path: String) {
        val file = File(preferencesManager.getDataProfile(path))
        file.delete()
    }

    fun exit(navController: NavHostController) {
        viewModelScope.launch {
            onCleanImages()
            navController.popBackStack()
        }
    }

}