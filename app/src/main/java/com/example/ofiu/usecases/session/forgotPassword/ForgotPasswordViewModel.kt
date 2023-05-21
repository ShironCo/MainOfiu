package com.example.ofiu.usecases.session.forgotPassword

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForgotPasswordViewModel: ViewModel() {
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

}