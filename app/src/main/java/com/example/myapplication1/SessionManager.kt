package com.example.myapplication1

import android.content.Context
import android.util.Log

object SessionManager {
    private const val PREFS_NAME = "auth_session"
    private const val KEY_EMAIL = "email"
    private const val KEY_TOKEN = "token"
    private const val KEY_SESSION_TIME = "session_time"

    fun saveSession(context: Context, email: String, token: String = "temp_token") {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_TOKEN, token)
            putLong(KEY_SESSION_TIME, System.currentTimeMillis())
            apply()
        }
        Log.d("SessionManager", "Сессия сохранена: $email")
    }

    fun getSession(context: Context): Pair<String, String>? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null)
        val token = prefs.getString(KEY_TOKEN, null)

        return if (email != null && token != null) {
            Log.d("SessionManager", "Сессия найдена: $email")
            Pair(email, token)
        } else {
            Log.d("SessionManager", "Сессия не найдена")
            null
        }
    }

    fun getUserEmail(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_EMAIL, null)
    }

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Log.d("SessionManager", "Сессия очищена")
    }

    fun isSessionActive(context: Context): Boolean {
        return getSession(context) != null
    }
}