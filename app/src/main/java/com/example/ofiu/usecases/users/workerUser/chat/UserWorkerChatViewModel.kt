package com.example.ofiu.usecases.users.workerUser.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.usecases.navigation.AppScreens
import com.example.ofiu.usecases.users.clientUser.chat.dto.Chats
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class UserWorkerChatViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {
    private val db = Firebase.firestore

    private val _idRecibe = preferencesManager.getDataProfile(Variables.IdPro.title)
    private val _idEnvia = MutableLiveData<String>()
    private val _name = MutableLiveData<String>()

    private val _chats = MutableLiveData<List<Chats>>()
    private val _imageProfile = MutableLiveData<String>()

    val chats: LiveData<List<Chats>> = _chats


    private val chatCollectionsRefInfor = db.collection(_idRecibe).orderBy("lastMinute", Query.Direction.DESCENDING)


    init {
        val chatQuery = chatCollectionsRefInfor
        chatQuery.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                println("Ocurrió un error al obtener los mensajes: $exception")
                return@addSnapshotListener
            }
            val listChats = snapshot?.mapNotNull {
                val name = it.getString("name")
                val previewMessage = it.getString("previewMessage")
                val imageProfile = it.getString("imageProfile")
                val idRecibe = it.getString("idRecibe")
                val idEnvia = it.getString("idEnvia")
                val lastMinute = it.getTimestamp("lastMinute")
                val fecha = if (lastMinute != null) {
                    val milliseconds = lastMinute.toDate().time
                    val date = Date(milliseconds)
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val month = calendar.get(Calendar.MONTH)
                    val year = calendar.get(Calendar.YEAR)
                    val monthD = month + 1
                    "$day/$monthD/$year"
                } else {
                    ""
                }
                if (name != null
                    && previewMessage != null
                    && imageProfile != null
                    && idRecibe != null
                    && idEnvia != null
                ) {
                    _idEnvia.value = idEnvia
                    _name.value = name
                    _imageProfile.value = imageProfile
                    Chats(
                        imageProfile = imageProfile,
                        name = name,
                        previewMessage = previewMessage,
                        lastMinute = fecha,
                        idEnvia = idEnvia,
                        idRecibe = idRecibe
                    )
                } else {
                    null
                }
            }.orEmpty().toMutableList()
            _chats.value = listChats
        }
    }

    fun onClickChat(
        navHostController: NavHostController,
        idPro: String,
        name: String,
        imageProfile: String
    ) {
        val idUser = _idEnvia.value
        val image = imageProfile.replace("/", "-")
        navHostController.navigate(AppScreens.Messages.route + "/$idPro/$idUser/$name/$image/$idUser")
    }

}