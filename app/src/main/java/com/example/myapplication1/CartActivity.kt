package com.example.myapplication1

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val backBtn = findViewById<Button>(R.id.backBtn)
        val cartListView = findViewById<ListView>(R.id.cartListView)
        val totalPriceText = findViewById<TextView>(R.id.totalPriceText)
        val checkoutBtn = findViewById<Button>(R.id.checkoutBtn)
        val clearCartBtn = findViewById<Button>(R.id.clearCartBtn)

        val adapter = CartItemAdapter(this, CartManager.cartItems) {
            updateTotal(totalPriceText)
            // Сохраняем после удаления
            CartManager.saveToStorage(this)
        }

        cartListView.adapter = adapter

        updateTotal(totalPriceText)

        backBtn.setOnClickListener {
            finish()
        }

        checkoutBtn.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Корзина пуста!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "✅ Заказ оформлен!", Toast.LENGTH_SHORT).show()
            CartManager.cartItems.clear()
            CartManager.saveToStorage(this)
            adapter.notifyDataSetChanged()
            updateTotal(totalPriceText)
        }

        clearCartBtn.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Корзина уже пуста", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CartManager.cartItems.clear()
            CartManager.saveToStorage(this)
            adapter.notifyDataSetChanged()
            updateTotal(totalPriceText)
            Toast.makeText(this, "❌ Корзина очищена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotal(totalPriceText: TextView) {
        var total = 0.0
        CartManager.cartItems.forEach { cartItem ->
            val product = CartManager.allProducts.find { it.id == cartItem.product_id }
            product?.let { total += it.price * cartItem.quantity }
        }
        totalPriceText.text = "Итого: ${"%.2f".format(total)} ₽"
    }

    override fun onPause() {
        super.onPause()
        CartManager.saveToStorage(this)
    }
}