package com.example.myapplication1

import android.content.Context
import android.util.Log

object ProfileDataManager {
    private const val PREFS_NAME = "profile_data"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_ADDRESS = "address"

    fun saveProfile(context: Context, firstName: String, lastName: String, address: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_FIRST_NAME, firstName)
            putString(KEY_LAST_NAME, lastName)
            putString(KEY_ADDRESS, address)
            apply()
        }
        Log.d("ProfileDataManager", "✅ Профиль сохранён: $firstName $lastName")
    }

    fun getFirstName(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_FIRST_NAME, "") ?: ""
    }

    fun getLastName(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LAST_NAME, "") ?: ""
    }

    fun getAddress(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ADDRESS, "") ?: ""
    }

    fun clearProfile(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Log.d("ProfileDataManager", "❌ Профиль очищен")
    }
}