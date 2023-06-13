package com.example.ofiu.usecases.menú

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.ofiu.UserRequest
import com.example.ofiu.usecases.navigation.AppScreens
import com.example.ofiu.usecases.navigation.DrawerScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    fun onChangeUser(user : String, context: Context, navController: NavHostController){
        viewModelScope.launch {
            val id = preferencesManager.getDataProfile(Variables.IdUser.title, "")
            repository.changeUser(user = UserRequest(id, user)).onSuccess {
                if (it.response == "cliente"){
                    navController.popBackStack()
                    navController.navigate(AppScreens.BottomBarScreen.route)
                }else{
                    navController.popBackStack()
                    navController.navigate(AppScreens.DrawerScreen.route)
                }
                preferencesManager.setDataProfile(Variables.Verify.title, it.response)
            }.onFailure {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
            }
        }
    }
}