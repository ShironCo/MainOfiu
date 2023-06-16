package com.example.ofiu.usecases.session.login

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.ofiu.LoginResponse
import com.example.ofiu.remote.dto.ofiu.LoginUserRequest
import com.example.ofiu.usecases.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    // LiveData para almacenar la respuesta del inicio de sesión
    private val _response = MutableLiveData<LoginResponse>()
    val response: LiveData<LoginResponse> = _response

    // LiveData para almacenar el valor del campo de correo electrónico
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    // LiveData para almacenar el valor del campo de contraseña
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    // LiveData para habilitar o deshabilitar la navegación hacia atrás
    private val _backEnable = MutableLiveData<Boolean>()
    val backEnable: LiveData<Boolean> = _backEnable

    // LiveData para controlar la visibilidad del botón de inicio de sesión
    private val _visibilityButton = MutableLiveData<Boolean>()
    val visibilityButton: LiveData<Boolean> = _visibilityButton

    // LiveData para controlar la visualización del indicador de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para habilitar o deshabilitar el botón de inicio de sesión
    private val _buttonValid = MutableLiveData<Boolean>()
    val buttonValid: LiveData<Boolean> = _buttonValid


    // Función para manejar los cambios en los campos de correo electrónico y contraseña
    fun onTextLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _buttonValid.value = validData(email, password)
    }

    // Función para validar los datos de inicio de sesión
    fun validData(email: String, password: String): Boolean {
        return (email.isNotBlank() && password.isNotBlank())
    }

    // Función para deshabilitar la navegación hacia atrás
    fun onBackEnable() {
        _backEnable.value = false
    }

    // Función para controlar la visibilidad del botón de contraseña
    fun onVisibilityButton() {
        _visibilityButton.value = _visibilityButton.value != true
    }

    // Función para realizar la operación de inicio de sesión
    fun onLoginSelected(
        email: String,
        password: String,
        navController: NavHostController,
        focusManager: FocusManager,
        context: Context
    ) {
        _isLoading.value = true
        _buttonValid.value = false
        viewModelScope.launch {
            repository.loginUser(LoginUserRequest(email, password)).onSuccess {
                // La solicitud de inicio de sesión fue exitosa
                _response.value = it
                if (_response.value?.successful.contentEquals("true")) {
                    // Inicio de sesión exitoso, realizar acciones adicionales
                    _password.value = ""
                    focusManager.clearFocus()
                    navController.popBackStack()
                    navController.navigate(AppScreens.Menu.route)
                    preferencesManager.setDataProfile(
                        Variables.IdUser.title,
                        _response.value!!.id.toString()
                    )
                    preferencesManager.setDataProfile(
                        Variables.NameUser.title,
                        _response.value!!.name.toString()
                    )
                    preferencesManager.setDataProfile(
                        Variables.EmailUser.title,
                        _response.value!!.email.toString()
                    )
                    preferencesManager.setDataProfile(Variables.PasswordUser.title, password)
                    preferencesManager.setDataProfile(
                        Variables.PhoneUser.title,
                        _response.value!!.phone.toString()
                    )
                    preferencesManager.setDataProfile(
                        Variables.ImageUser.title,
                        _response.value!!.img.toString()
                    )
                    preferencesManager.setDataProfile(Variables.LoginActive.title, "true")
                    _buttonValid.value = false
                }
            }.onFailure {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
               // _response.value = LoginResponse("Error $it", null, null, null, null)
            }
            _isLoading.value = false
            _buttonValid.value = true
        }
    }


}