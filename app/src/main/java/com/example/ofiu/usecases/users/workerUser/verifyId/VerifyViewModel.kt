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
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    private val _changeView = MutableLiveData<Boolean>()
    val changeView: LiveData<Boolean> = _changeView

    private val _response = MutableLiveData<UserResponse>()
    val response: LiveData<UserResponse> = _response

    private val _nameImage = MutableLiveData<String>()

    private val _previewImage = MutableLiveData<ByteArray>()
    val previewView: LiveData<ByteArray> = _previewImage

    fun onTextChange(name: String) {
        _nameImage.value = name
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
                if (_nameImage.value!!.equals(NameImages.ImageTrasera.image)) {
                    navController.popBackStack()
                } else if (_nameImage.value!!.equals(NameImages.ImageFace.image)) {

                }
            }
        }
    }

    fun getDataPreference(key: String): String {
        return preferencesManager.getDataProfile(key, "")
    }

    fun SendImages(context: Context) {
        viewModelScope.launch {
            ConvertImage(NameImages.ImageFrontal.image).onSuccess {
                _image1.value = it
            }
            ConvertImage(NameImages.ImageTrasera.image).onSuccess {
                _image2.value = it
            }
            ConvertImage(NameImages.ImageFace.image).onSuccess {
                _image3.value = it
            }
            repository.sendImage(_image1.value!!, _image2.value!!, _image3.value!!).onSuccess {
                _response.value = it
            }.onFailure {
                _response.value = UserResponse(it.toString())
            }

            Toast.makeText(context, _response.value.toString(), Toast.LENGTH_LONG).show()

            //FALTA BORRAR LA IMAGEN DEL DISPOSITIVO
            preferencesManager.setDataProfile(NameImages.ImageFrontal.image, "")
            preferencesManager.setDataProfile(NameImages.ImageTrasera.image, "")
            preferencesManager.setDataProfile(NameImages.ImageFace.image, "")

        }
    }

    fun CancelSend() {
        preferencesManager.setDataProfile(NameImages.ImageFrontal.image, "")
        preferencesManager.setDataProfile(NameImages.ImageTrasera.image, "")
        preferencesManager.setDataProfile(NameImages.ImageFace.image, "")
    }

    private fun ConvertImage(name: String): Result<MultipartBody.Part> {
        return try {
            val imageUrl = preferencesManager.getDataProfile(name, "")
            val file = File(imageUrl)
            val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray)
            val requestFile = MultipartBody.Part.createFormData(name, "image/jpeg", requestBody)
            Result.success(requestFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}