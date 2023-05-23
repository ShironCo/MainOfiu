package com.example.ofiu.usecases.session.login.forgotPassword

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(): ViewModel() {
    //Primer paso
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _buttonValidation = MutableLiveData<Boolean>()
    val buttonValidation : LiveData<Boolean> = _buttonValidation

    fun onTextChange(email:String){
        _email.value = email
        _buttonValidation.value = isValidEmail(email)
    }

    private fun isValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    //Segundo paso
    private val _code = MutableLiveData<String>()
    val code : LiveData<String> = _code


    fun onTextChangeTwo(code: String){
        _code.value = code
        _buttonValidation.value = isValidCode(code)
    }

    private fun isValidCode(code: String): Boolean{
       return code.isNotEmpty() && (code.length == 6)
    }

    //Tercer paso

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _passwordRepeat = MutableLiveData<String>()
    val passwordRepeat : LiveData<String> = _passwordRepeat

    private val _changeStep = MutableLiveData<Boolean>()
    val changeStep : LiveData<Boolean> = _changeStep

    private val _visibilityButton = MutableLiveData<Boolean>()
    val visibilityButton : LiveData<Boolean> = _visibilityButton

    fun onTextChangeThree(password: String, passwordRepeat: String){
        _password.value = password
        _passwordRepeat.value = passwordRepeat
        _buttonValidation.value = isValidPassword(password) && (password == passwordRepeat)
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = "^(?=.*[!@#\$%^&*()-+])(?=.{8,})[a-zA-Z0-9!@#\$%^&*()-+]+$".toRegex()
        return pattern.matches(password)
    }

     fun onStepChange(){
        _changeStep.value = _changeStep.value != true
    }
    fun onVisibilityButton(){
        _visibilityButton.value = _visibilityButton.value != true
    }
}