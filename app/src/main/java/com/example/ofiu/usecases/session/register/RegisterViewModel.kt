package com.example.ofiu.usecases.session.register

import android.util.Patterns
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

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _lastName = MutableLiveData<String>()
    val lastName : LiveData<String> = _lastName

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _phone = MutableLiveData<String>()
    val phone : LiveData<String> = _phone

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _passwordRepeat = MutableLiveData<String>()
    val passwordRepeat : LiveData<String> = _passwordRepeat

    private val _passwordEnable = MutableLiveData<Boolean>()
    val passwordEnable : LiveData<Boolean> = _passwordEnable

    private val _passwordVal = MutableLiveData<Boolean>()
    val passwordVal : LiveData<Boolean> = _passwordVal


    private val _visibility = MutableLiveData<Boolean>()
    val visibility : LiveData<Boolean> = _visibility

    fun onTextChange(name:String, lastName:String, email:String, phone:String, password:String, passwordRepeat:String){
        _name.value = name
        _lastName.value = lastName
        _email.value = email
        _phone.value = phone
        _password.value = password
        _passwordRepeat.value = passwordRepeat
        _passwordEnable.value = isValidPassword(password)
        _passwordVal.value = isValidPasswordRepeat(password, passwordRepeat)
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = "^(?=.*[!@#\$%^&*()-+])(?=.{8,})[a-zA-Z0-9!@#\$%^&*()-+]+$".toRegex()
        return pattern.matches(password)
    }
    private fun isValidPasswordRepeat(password: String, passwordRepeat: String): Boolean {
        val pasVal = (password == passwordRepeat)
        return pasVal
    }

    fun onVisibility(){
        _visibility.value = _visibility.value != true
    }

    fun onButtonClick(){

    }
}