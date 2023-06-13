package com.example.ofiu.usecases.users.clientUser.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.usecases.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserClientProfileViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _expandMenu = MutableLiveData<Boolean>()
    val expandMenu : LiveData<Boolean> = _expandMenu

    fun onExpandMenu(){
        _expandMenu.value = _expandMenu.value != true
    }

    fun onCloseSession(navController: NavController){
        preferencesManager.setDataProfile(Variables.EmailUser.title, "")
        preferencesManager.setDataProfile(Variables.PasswordUser.title, "")
        preferencesManager.setDataProfile(Variables.LoginActive.title, "false")
        preferencesManager.setDataProfile(Variables.DescriptionPro.title, "")
        navController.popBackStack()
        navController.navigate(AppScreens.Session.route)
    }

    fun onChangeAccount(navController: NavController){

        navController.popBackStack()
        navController.navigate(AppScreens.DrawerScreen.route)
    }
}