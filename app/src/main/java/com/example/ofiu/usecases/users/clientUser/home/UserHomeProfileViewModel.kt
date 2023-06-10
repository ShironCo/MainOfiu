package com.example.ofiu.usecases.users.clientUser.home

import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserHomeProfileViewModel @Inject constructor(

): ViewModel() {

    private val _searchString = MutableLiveData<String>()
    val searchString : LiveData<String> = _searchString

    fun onTextChange(searchString: String){
        _searchString.value = searchString
    }

}