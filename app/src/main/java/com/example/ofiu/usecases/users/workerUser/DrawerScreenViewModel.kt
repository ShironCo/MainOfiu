package com.example.ofiu.usecases.users.workerUser

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
class DrawerScreenViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _moreVertToggle = MutableLiveData<Boolean>()
    val moreVertToggle: LiveData<Boolean> = _moreVertToggle

    private val _showVertMore = MutableLiveData<Boolean>()
    val showVertMore: LiveData<Boolean> = _showVertMore

    private val _backHandler = MutableLiveData<Boolean>()
    val backHandler: LiveData<Boolean> = _backHandler

    fun setMoreVertToogle(value: Boolean){
        _moreVertToggle.value = value
    }
    fun setShowVertMore(value: Boolean){
        _showVertMore.value = value
    }

    fun onBackHandler(value : Boolean){
        _backHandler.value = value
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
        navController.navigate(AppScreens.BottomBarScreen.route)
    }

}