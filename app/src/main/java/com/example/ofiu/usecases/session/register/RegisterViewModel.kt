package com.example.ofiu.usecases.session.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel(){
    //Botones vista LEGAL
    private val _buttonLegal = MutableLiveData<Boolean>()
    val buttonLegal : LiveData<Boolean> = _buttonLegal

    private val _backEnable = MutableLiveData<Boolean>()
    val backEnable : LiveData<Boolean> = _backEnable

    fun onBackEnable(){
        _backEnable.value = false
    }

    fun onButtonLegal(){
        _buttonLegal.value = _buttonLegal.value != true
    }

    //Botones vista REGISTER



}