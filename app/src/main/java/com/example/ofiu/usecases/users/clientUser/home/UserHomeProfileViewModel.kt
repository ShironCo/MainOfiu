package com.example.ofiu.usecases.users.clientUser.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import com.example.ofiu.remote.dto.ofiu.professionals.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeProfileViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String> = _searchString

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        // Se obtiene el ID del usuario desde las preferencias
        val id = preferencesManager.getDataProfile(Variables.IdUser.title, "")
        _isLoading.value = true
        viewModelScope.launch {
            // Se obtienen los usuarios profesionales y se asignan a la lista de usuarios
            repository.getUsersPro(id, "").onSuccess {
                _users.value = it.users
            }.onFailure {
                println(it.message)
            }
            _isLoading.value = false
        }
    }

    fun onSearchButton(search: String) {
        // Se obtiene el ID del usuario desde las preferencias
        val id = preferencesManager.getDataProfile(Variables.IdUser.title, "")
        _isLoading.value = true
        search.let {
                viewModelScope.launch {
                    // Se obtienen los usuarios profesionales según el término de búsqueda y se asignan a la lista de usuarios
                    repository.getUsersPro(id, it).onSuccess {
                        _users.postValue(it.users)
                        println(it.users)
                    }.onFailure {
                        println(it.message)
                    }
                    _isLoading.value = false
                }
        }
    }

    fun onTextChange(searchString: String) {
        // Se actualiza el valor de búsqueda cuando se cambia el texto en el campo de búsqueda
        _searchString.value = searchString
    }
}
