package com.example.ofiu.usecases.users.clientUser.home.details.reporting

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ofiu.Preferences.PreferencesManager
import com.example.ofiu.Preferences.Variables
import com.example.ofiu.domain.OfiuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: OfiuRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _report = MutableLiveData<String>()
    val report : LiveData<String> = _report

    fun onTextChange(report: String){
        _report.value = report
    }

    fun reportUser(
        navHostController: NavHostController,
        idPro: String?,
        context: Context
    ) {
        val idUser = preferencesManager.getDataProfile(Variables.IdUser.title)
        if (_report.value != null && idPro != null) {
            viewModelScope.launch {
                repository.reportUser(
                    idprof = idPro,
                    iduser = idUser,
                    deta = _report.value!!
                ).onSuccess {
                    if(it.response == "true"){
                        navHostController.popBackStack()
                    }
                }.onFailure {

                }
            }
        }
    }


}