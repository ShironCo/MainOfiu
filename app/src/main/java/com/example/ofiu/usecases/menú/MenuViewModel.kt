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

            // Obtener el ID de usuario almacenado en las preferencias compartidas
            val id = preferencesManager.getDataProfile(Variables.IdUser.title, "")

            // Llamar a la función changeUser del repositorio para cambiar el usuario
            repository.changeUser(user = UserRequest(id, user)).onSuccess {

                // El tipo de respuesta es "cliente", navegar a la pantalla BottomBarScreen
                // usando el NavController
                if (it.response == "cliente"){
                    // El tipo de respuesta no es "cliente", navegar a la pantalla DrawerScreen
                    // usando el NavController
                    navController.popBackStack()
                    navController.navigate(AppScreens.BottomBarScreen.route)
                }else{

                    // Guardar el valor de respuesta en las preferencias compartidas
                    navController.popBackStack()
                    navController.navigate(AppScreens.DrawerScreen.route)
                }
                preferencesManager.setDataProfile(Variables.Verify.title, it.response)
            }.onFailure {
                // Ocurrió un error, mostrar un mensaje de error utilizando un Toast

                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
            }
        }
    }
}