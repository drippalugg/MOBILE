package com.example.myapplication1

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import android.util.Log

object CartDataManager {
    private const val PREFS_NAME = "cart_data"
    private const val KEY_CART_ITEMS = "cart_items"

    fun saveCart(context: Context, cartItems: List<CartItem>) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val jsonString = json.encodeToString(cartItems)

            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().apply {
                putString(KEY_CART_ITEMS, jsonString)
                apply()
            }
            Log.d("CartDataManager", " Корзина сохранена: ${cartItems.size} товаров")
        } catch (e: Exception) {
            Log.e("CartDataManager", " Ошибка сохранения корзины: ${e.message}")
        }
    }

    fun loadCart(context: Context): List<CartItem> {
        return try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonString = prefs.getString(KEY_CART_ITEMS, null)

            if (jsonString != null) {
                val json = Json { ignoreUnknownKeys = true }
                val items = json.decodeFromString<List<CartItem>>(jsonString)
                Log.d("CartDataManager", " Корзина загружена: ${items.size} товаров")
                items
            } else {
                Log.d("CartDataManager", " Сохранённая корзина не найдена")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("CartDataManager", " Ошибка загрузки корзины: ${e.message}")
            emptyList()
        }
    }

    fun clearCart(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Log.d("CartDataManager", " Корзина очищена")
    }
}

object CartManager {
    val cartItems = mutableListOf<CartItem>()
    val allProducts = mutableListOf<Product>()

    fun loadFromStorage(context: Context) {
        cartItems.clear()
        cartItems.addAll(CartDataManager.loadCart(context))
    }

    fun saveToStorage(context: Context) {
        CartDataManager.saveCart(context, cartItems)
    }
}
