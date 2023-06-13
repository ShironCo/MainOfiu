package com.example.ofiu.usecases.users.clientUser.chat.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(): ViewModel(){
    private val _message = MutableLiveData<String>()
    val message : LiveData<String> = _message

    fun onMessageChange(message: String){
        _message.value = message
    }
}