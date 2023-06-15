package com.example.ofiu.usecases.users.clientUser.chat.messages


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.usecases.users.clientUser.chat.dto.MessageChat
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

    private val db = Firebase.firestore

    private val _listMessages = MutableLiveData<List<MessageChat>>()
    val listMessages: LiveData<List<MessageChat>> = _listMessages

    private val _imageProfilePro = MutableLiveData<String>()

    //CHAT USER


    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun newChats(idPro: String, idUser: String) {
        val chatCollectionsRef = db.collection(idUser).document(idPro).collection("Messages")
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
        idPro: String,
        idSender: String
    ) {
        val nameUser = preferencesManager.getDataProfile(Variables.NameUser.title)
        val imageProfile = _imageProfilePro.value
        val imageUser = preferencesManager.getDataProfile(Variables.ImageUser.title)

        val chatData = hashMapOf(
            "message" to message,
            "sender" to idSender,
            "timestamp" to FieldValue.serverTimestamp()
        )

        val chatDocumentRef = db.collection(idUser).document(idPro)
        chatDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                chatDocumentRef.update("previewMessage", message)
                    .addOnSuccessListener {
                    }.addOnFailureListener { exception ->
                        println("Error al actualizar el mensaje: $exception")
                    }
            } else {
                val chatUser = hashMapOf(
                    "imageProfile" to imageProfile,
                    "name" to name,
                    "previewMessage" to message,
                    "lastMinute" to FieldValue.serverTimestamp(),
                    "idRecibe" to idPro,
                    "idEnvia" to idUser
                )
                chatDocumentRef.set(chatUser)
                    .addOnSuccessListener {
                        _message.value = ""
                    }.addOnFailureListener { exception ->
                        println("Error al crear el chat: $exception")
                    }
            }
        }.addOnFailureListener { exception ->
            // Error al obtener el documento del chat
            println("Error al obtener el documento del chat: $exception")
        }

        val chatOtherUserDocumentRef = db.collection(idPro).document(idUser)
        chatOtherUserDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                chatOtherUserDocumentRef.update("previewMessage", message).addOnSuccessListener {
                }.addOnFailureListener {
                    println("Error al actualizar el mensaje: $it")
                }
            } else {
                val chatPro = hashMapOf(
                    "imageProfile" to imageUser,
                    "name" to nameUser,
                    "previewMessage" to message,
                    "lastMinute" to FieldValue.serverTimestamp(),
                    "idRecibe" to idUser,
                    "idEnvia" to idPro
                )
                chatOtherUserDocumentRef.set(chatPro)
                    .addOnSuccessListener {
                        // Actualización exitosa en el chat del otro usuario
                    }.addOnFailureListener { exception ->
                        // Error al actualizar el chat del otro usuario
                        println("Error al actualizar el chat del otro usuario: $exception")
                    }
            }
        }.addOnFailureListener {exception ->
            println("Error al obtener el documento del chat: $exception")
        }

        val chatCollectionRef = db.collection(idUser).document(idPro).collection("Messages")
        chatCollectionRef.add(chatData)
            .addOnSuccessListener {
                _message.value = ""
            }.addOnFailureListener {
                println("Error al enviar el mensaje: $it")
            }

    }
}