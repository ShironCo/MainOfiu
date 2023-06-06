package com.example.ofiu.Preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.ofiu.remote.dto.LoginResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferencesProfile: SharedPreferences =
        context.getSharedPreferences("data_profile", Context.MODE_PRIVATE)

    fun getDataProfile(key: String, defaultValue: String = ""): String {
        return sharedPreferencesProfile.getString(key, defaultValue) ?: defaultValue
    }

    fun setDataProfile(key: String, value: String) {
        with(sharedPreferencesProfile.edit()) {
            putString(key, value)
            apply()
        }
    }

}
