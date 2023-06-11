package com.example.ofiu.usecases.session.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.ofiu.RegisterUserRequest
import com.example.ofiu.remote.dto.ofiu.UserResponse
import com.example.ofiu.usecases.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: OfiuRepository
) : ViewModel() {
    //Botones vista LEGAL
    private val _buttonLegal = MutableLiveData<Boolean>()
    val buttonLegal: LiveData<Boolean> = _buttonLegal

    private val _backEnable = MutableLiveData<Boolean>()
    val backEnable: LiveData<Boolean> = _backEnable

    fun onBackEnable() {
        _backEnable.value = false
    }

    fun onButtonLegal() {
        _buttonLegal.value = _buttonLegal.value != true
    }

    //Botones vista REGISTER

    private val _response = MutableLiveData<UserResponse>()
    val response: LiveData<UserResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> = _lastName

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _passwordRepeat = MutableLiveData<String>()
    val passwordRepeat: LiveData<String> = _passwordRepeat

    private val _passwordEnable = MutableLiveData<Boolean>()
    val passwordEnable: LiveData<Boolean> = _passwordEnable

    private val _passwordVal = MutableLiveData<Boolean>()
    val passwordVal: LiveData<Boolean> = _passwordVal

    private val _visibility = MutableLiveData<Boolean>()
    val visibility: LiveData<Boolean> = _visibility

    private val _validButton = MutableLiveData<Boolean>()
    val validButton: LiveData<Boolean> = _validButton

    fun onTextChange(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        passwordRepeat: String
    ) {
        _name.value = name
        _lastName.value = lastName
        _email.value = email
        _phone.value = phone
        _password.value = password
        _passwordRepeat.value = passwordRepeat
        _passwordEnable.value = isValidPassword(password)
        _passwordVal.value = isValidPasswordRepeat(password, passwordRepeat)
        _validButton.value = isValidData(name, lastName, email, phone, password, passwordRepeat)
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%?&]{8,}$""".toRegex()
        return pattern.matches(password)
    }

    private fun isValidPasswordRepeat(password: String, passwordRepeat: String): Boolean {
        val pasVal = (password == passwordRepeat)
        return pasVal
    }

    private fun isValidData(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        passwordRepeat: String
    ): Boolean {
        return (name.isNotBlank() && lastName.isNotBlank()
                && email.isNotBlank() && phone.isNotBlank()
                && password.isNotBlank()
                && passwordRepeat.isNotBlank() && isValidPasswordRepeat(password, passwordRepeat)
                && phone.length == 10)
    }

    fun onVisibility() {
        _visibility.value = _visibility.value != true
    }

    fun onButtonClick(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        navHostController: NavController
    ) {
        _validButton.value = false
        _isLoading.value = true
        viewModelScope.launch {
            val user = RegisterUserRequest(
                name, lastName, email, phone, password
            )
            repository.addUser(user).onSuccess {
                _response.value = it
                if (it.response == "true"){
                    delay(4000)
                    navHostController.popBackStack()
                    navHostController.navigate(AppScreens.Session.route)
                }
            }.onFailure {
                _response.value = UserResponse("Es un error $it")
            }
            _isLoading.value = false
            _validButton.value = true
        }
    }
}