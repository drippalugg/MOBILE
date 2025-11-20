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
        val cartListView = findViewById<ListView>(R.id.cartList)
        val totalPriceText = findViewById<TextView>(R.id.cartTotal)
        val checkoutBtn = findViewById<Button>(R.id.checkoutCartBtn)

        val adapter = CartItemAdapter(
            context = this@CartActivity,
            cartItems = CartManager.cartItems,
            allProducts = CartManager.allProducts,
            onQuantityChanged = {
                updateTotal(totalPriceText)
                CartManager.saveToStorage(this@CartActivity)
            }
        )

        cartListView.adapter = adapter

        updateTotal(totalPriceText)

        backBtn.setOnClickListener {
            finish()
        }

        checkoutBtn.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this@CartActivity, "Корзина пуста!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this@CartActivity, "Заказ оформлен!", Toast.LENGTH_SHORT).show()
            CartManager.cartItems.clear()
            CartManager.saveToStorage(this@CartActivity)
            adapter.notifyDataSetChanged()
            updateTotal(totalPriceText)
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