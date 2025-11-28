// Archivo: SessionManager.kt

package com.example.pruewba.Modelo

import android.content.Context
import android.content.SharedPreferences

class SesionManager(context: Context) {
    private val PREF_NAME = "PCStatusSession"
    private val IS_LOGGED_IN = "isLoggedIn"
    private val KEY_USER_ID = "userId"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    // 1. Guardar la sesión al iniciar sesión
    fun createLoginSession(userId: Int) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, userId)
        editor.apply()
    }

    // 2. Obtener el estado de la sesión
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    // 3. Obtener el ID del usuario (necesario para las consultas)
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1) // -1 si no existe
    }

    // 4. Cerrar la sesión
    fun logout() {
        editor.clear()
        editor.apply()
    }
}