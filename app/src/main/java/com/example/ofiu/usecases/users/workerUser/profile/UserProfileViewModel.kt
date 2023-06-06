package com.example.ofiu.usecases.users.workerUser.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(): ViewModel() {

    private val _toggleDesc = MutableLiveData<Boolean>()
    val toggleDesc : LiveData<Boolean> = _toggleDesc

    private val _desc = MutableLiveData<String>()
    val desc : LiveData<String> = _desc

    fun onTextChange(desc: String){
        _desc.value = desc
    }

    fun onSetToggleDesc(value : Boolean){
        _toggleDesc.value = value
    }

}