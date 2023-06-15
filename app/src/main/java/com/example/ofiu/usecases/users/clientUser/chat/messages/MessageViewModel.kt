package com.example.ofiu.usecases.users.clientUser.chat.messages

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.usecases.users.clientUser.chat.dto.MessageChat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _idUser = preferencesManager.getDataProfile(Variables.IdUser.title)

    private val db = Firebase.firestore

    private val _listMessages = MutableLiveData<List<MessageChat>>()
    val listMessages: LiveData<List<MessageChat>> = _listMessages

    private val _imageProfilePro = MutableLiveData<String>()

    //CHAT USER


    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun newChats(idPro: String) {
        val chatCollectionsRef = db.collection(_idUser).document(idPro).collection("Messages")
        if (idPro.isNotEmpty()) {
            val chatMessagesQuery =
                chatCollectionsRef.orderBy("timestamp", Query.Direction.DESCENDING)
            chatMessagesQuery.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    println("Ocurrió un error al obtener los mensajes: $exception")
                    return@addSnapshotListener
                }
                val listMessages = snapshot?.mapNotNull { document ->
                    val message = document.getString("message")
                    val sender = document.getString("sender")
                    val timestamp = document.getTimestamp("timestamp")
                    val hora = if (timestamp != null) {
                        val milliseconds = timestamp.toDate().time
                        val date = Date(milliseconds)
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        val hour = calendar.get(Calendar.HOUR_OF_DAY)
                        val minute = calendar.get(Calendar.MINUTE)
                        "$hour:$minute"
                    } else {
                        ""
                    }
                    if (message != null && timestamp != null && sender != null) {
                        MessageChat(message, hora, sender)
                    } else {
                        null
                    }
                }.orEmpty().toMutableList()
                _listMessages.value = listMessages
            }
        }
    }

    fun setIdUser(idPro: String, imageProfile: String) {
        _imageProfilePro.value = imageProfile
    }

    fun onMessageChange(message: String) {
        _message.value = message
    }

    fun onSendMessage(
        name: String,
        message: String,
        idUser: String,
        idPro: String
    ) {
        val nameUser = preferencesManager.getDataProfile(Variables.NameUser.title)
        val imageProfile = _imageProfilePro.value
        val chatData = hashMapOf(
            "message" to message,
            "sender" to idUser,
            "timestamp" to FieldValue.serverTimestamp()
        )
        val chatUser = hashMapOf(
            "imageProfile" to imageProfile,
            "name" to name,
            "previewMessage" to message,
            "lastMinute" to FieldValue.serverTimestamp(),
            "id" to idPro
        )
        val chatPro = hashMapOf(
            "imageProfile" to "https://concepto.de/wp-content/uploads/2013/08/cliente-e1551799486636.jpg",
            "name" to nameUser,
            "previewMessage" to message,
            "lastMinute" to FieldValue.serverTimestamp(),
            "id" to idUser
        )
        val chatCollectionRef = db.collection(_idUser).document(idPro).collection("Messages")
        chatCollectionRef.add(chatData)
            .addOnSuccessListener {
                _message.value = ""
            }.addOnFailureListener {
                println("Error al enviar el mensaje: $it")
            }

        idPro.let {
            db.collection(idUser).document(it).set(chatUser)
                .addOnSuccessListener {
                }.addOnFailureListener {
                    println("Error al enviar el mensaje: $it")
                }
        }
        idPro.let {
            db.collection(it).document(idUser).set(chatPro)
                .addOnSuccessListener {
                }.addOnFailureListener {
                    println("Error al enviar el mensaje: $it")
                }
        }
    }
}