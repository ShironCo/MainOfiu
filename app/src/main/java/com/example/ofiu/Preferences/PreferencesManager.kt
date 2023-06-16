package com.example.ofiu.Preferences

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    // Se inicializa el objeto SharedPreferences utilizando el contexto proporcionado

    private val sharedPreferencesProfile: SharedPreferences =
        context.getSharedPreferences("data_profile", Context.MODE_PRIVATE)


    // Obtiene un valor almacenado en las preferencias compartidas con la clave especificada
    // Si no se encuentra ningún valor para la clave, se devuelve el valor predeterminado proporcionado

    fun getDataProfile(key: String, defaultValue: String = ""): String {
        return sharedPreferencesProfile.getString(key, defaultValue) ?: defaultValue
    }

    // Guarda un valor en las preferencias compartidas con la clave especificada
    fun setDataProfile(key: String, value: String) {
        with(sharedPreferencesProfile.edit()) {
            putString(key, value)
            apply() // Se aplica el cambio de forma asíncrona
        }
    }
}
