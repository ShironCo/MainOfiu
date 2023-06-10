package com.example.ofiu.usecases.splash

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.LoginResponse
import com.example.ofiu.remote.dto.LoginUserRequest
import com.example.ofiu.usecases.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
): ViewModel(
) {

    private val _internetAvaible = MutableLiveData<Boolean>()
    val internetAvaible : LiveData<Boolean> = _internetAvaible

    private val _response = MutableLiveData<LoginResponse>()
    val response : LiveData<LoginResponse> = _response

    private val _startAnimation = MutableLiveData<Boolean>()
    val startAnimation : LiveData<Boolean> = _startAnimation


    fun setStartAnimation(value : Boolean){
        _startAnimation.value = value
    }

    fun setNav (navController: NavHostController, context: Context) {
        val email = preferencesManager.getDataProfile(Variables.EmailUser.title)
        val password = preferencesManager.getDataProfile(Variables.PasswordUser.title)
        val loginChange = preferencesManager.getDataProfile(Variables.LoginActive.title)
            if (email.isNotBlank() && password.isNotBlank() && loginChange == "true") {
                viewModelScope.launch {
                    repository.loginUser(LoginUserRequest(email, password)).onSuccess {
                        if (it.successful == "true") {
                            navController.popBackStack()
                            navController.navigate(AppScreens.Menu.route)
                        }
                    }.onFailure {
                        Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                        navController.navigate(AppScreens.Menu.route)
                    }
                }
            } else {
                navController.popBackStack()
                navController.navigate(AppScreens.Session.route)
            }
        }
}