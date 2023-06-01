package com.example.ofiu.usecases.users.workerUser.verifyId

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
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
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.domain.OfiuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _changeView = MutableLiveData<Boolean>()
    val changeView: LiveData<Boolean> = _changeView

    private val _nameImage = MutableLiveData<String>()

    private val _previewImage = MutableLiveData<ByteArray>()
    val previewView : LiveData<ByteArray> = _previewImage

    fun onTextChange(name : String){
        _nameImage.value = name
    }

    fun onChangeView(){
        _changeView.value = _changeView.value != true
    }

    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModelScope.launch {
            repository.showCameraPreview(previewView, lifecycleOwner)
        }
    }

    fun onCleanPreviewImage(){
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
                                onChangeView()
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

    fun onSaveImage(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val bitmapImage = BitmapFactory.decodeByteArray(_previewImage.value, 0, _previewImage.value!!.size, null)
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
                onChangeView()
                onCleanPreviewImage()
            }
        }
    }

    fun getDataPreference(key: String): String{
        return preferencesManager.getDataProfile(key, "")
    }

}