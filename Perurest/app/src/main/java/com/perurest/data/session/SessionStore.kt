package com.perurest.session

import android.content.Context
import android.content.SharedPreferences

class SessionStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("perurest_session", Context.MODE_PRIVATE)

    fun saveLoggedUser(email: String) {
        prefs.edit().putString(KEY_EMAIL, email).apply()
    }

    fun getLoggedUser(): String? = prefs.getString(KEY_EMAIL, null)

    fun clear() {
        prefs.edit().remove(KEY_EMAIL).apply()
    }

    companion object {
        private const val KEY_EMAIL = "logged_email"
    }
}
