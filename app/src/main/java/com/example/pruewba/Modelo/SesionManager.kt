package com.example.pruewba.Modelo

import android.content.Context
import android.content.SharedPreferences

class SesionManager(context: Context) {
    private val PREF_NAME = "PCStatusSession"
    private val IS_LOGGED_IN = "isLoggedIn"
    private val KEY_USER_ID = "userId"
    // Usamos MODE_PRIVATE para que solo tu app pueda acceder a las preferencias
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun createLoginSession(userId: Int) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putInt(KEY_USER_ID, userId)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1) // Retorna -1 si no hay ID guardado
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}