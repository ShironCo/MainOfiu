package com.example.ofiu.usecases.session.register.legal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LegalViewModel @Inject constructor(): ViewModel() {

    private val _expandMenu = MutableLiveData<Boolean>()
    val expandMenu : LiveData<Boolean> = _expandMenu

    fun onChangeExpandMenu(){
        _expandMenu.value = _expandMenu.value != true
    }






}