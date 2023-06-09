package com.example.ofiu.usecases.users.workerUser.verifyId

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    private val _image1 = MutableLiveData<MultipartBody.Part>()
    private val _image2 = MutableLiveData<MultipartBody.Part>()
    private val _image3 = MutableLiveData<MultipartBody.Part>()

    private val _sDK28 = MutableLiveData<Boolean>()
    val sDK28 : LiveData<Boolean> = _sDK28

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

    private val _verifySuccessful = MutableLiveData<Boolean>()

    fun verifySuccessful(): Boolean{
        val verify = preferencesManager.getDataProfile(Variables.Verify.title)
        return (verify == "verificado")
    }
    fun setSDK(value: Boolean){
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
    fun onValidButtonId(){
        val image1: String = preferencesManager.getDataProfile(Variables.ImageFrontal.title)
        val image2: String = preferencesManager.getDataProfile(Variables.ImageTrasera.title)
        if (image1.isNotBlank()){
            _validButtonId.value = false
            _validButtonFace.value = true
        }
        if (image2.isNotBlank()){
            _validButtonFace.value = false
        }
    }

    fun onValidButtonFace(){
        val image1: String = preferencesManager.getDataProfile(Variables.ImageFrontal.title)
        val image2: String = preferencesManager.getDataProfile(Variables.ImageTrasera.title)
        val image3: String = preferencesManager.getDataProfile(Variables.ImageFace.title)
        if (image1.isNotBlank() && image2.isNotBlank()){
            _validButtonId.value = false
            _validButtonFace.value = image3.isBlank()
        }else{
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
                }else if (_nameImage.value!! == Variables.ImageFace.title){
                    navController.popBackStack()
                    _validButtonFace.value = false
                }
            }
        }
    }

    fun onSendImages(context: Context, navController: NavHostController) {
        _validButtonId.value = true
        viewModelScope.launch {
            ConvertImage(Variables.ImageFrontal.title).onSuccess {
                _image1.value = it
            }
            ConvertImage(Variables.ImageTrasera.title).onSuccess {
                _image2.value = it
            }
            ConvertImage(Variables.ImageFace.title).onSuccess {
                _image3.value = it
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val id = preferencesManager.getDataProfile(Variables.IdUser.title)
            val requestBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
            val result = repository.sendImage(_image1.value!!, _image2.value!!, _image3.value!!, requestBody)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    onCleanImages()
                    _showAlertDialog.value = 1
                    delay(4000)
                    navController.popBackStack()
                }.onFailure {
                    Toast.makeText(context, "Error $it", Toast.LENGTH_LONG).show()
                    _validButtonId.value = false
                }
            }
        }
    }

    fun onCleanImages(){
        DeleteImage(Variables.ImageFrontal.title)
        preferencesManager.setDataProfile(Variables.ImageFrontal.title, "")
        DeleteImage(Variables.ImageTrasera.title)
        preferencesManager.setDataProfile(Variables.ImageTrasera.title, "")
        DeleteImage(Variables.ImageFace.title)
        preferencesManager.setDataProfile(Variables.ImageFace.title, "")
    }

    private fun ConvertImage(name: String): Result<MultipartBody.Part> {
        return try {
            val imageUrl = preferencesManager.getDataProfile(name, "")
            val file = File(imageUrl)
            val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val requestBody =
                byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0)
            val requestFile = MultipartBody.Part.createFormData(name, "image/jpeg", requestBody)
            Result.success(requestFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    private fun DeleteImage(path: String) {
        val file = File(preferencesManager.getDataProfile(path))
        file.delete()
        }
}