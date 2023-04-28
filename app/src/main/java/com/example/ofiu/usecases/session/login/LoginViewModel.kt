package com.example.ofiu.usecases.session.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel(){


    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _backEnable = MutableLiveData<Boolean>()
    val backEnable : LiveData<Boolean> = _backEnable

    private val _visibilityButton = MutableLiveData<Boolean>()
    val visibilityButton : LiveData<Boolean> = _visibilityButton

    fun onTextLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
    }

    fun onBackEnable(){
        _backEnable.value = false
    }

    fun onVisibilityButton(){
        _visibilityButton.value = _visibilityButton.value != true
    }

    fun onLoginSelected() {

    }


}