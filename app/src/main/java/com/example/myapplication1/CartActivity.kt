package com.example.myapplication1

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var adapter: CartItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val cartList: ListView = findViewById(R.id.cartList)
        val totalText: TextView = findViewById(R.id.cartTotal)
        val backBtn: Button = findViewById(R.id.backBtn)
        val checkoutBtn: Button = findViewById(R.id.checkoutCartBtn)

        // Инициализируется адаптер
        adapter = CartItemAdapter(
            this,
            CartManager.cartItems,
            CartManager.allProducts
        ) {
            updateTotal(totalText)
            adapter.notifyDataSetChanged()
        }

        cartList.adapter = adapter
        updateTotal(totalText)

        backBtn.setOnClickListener {
            finish()
        }

        checkoutBtn.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show()
            } else {
                val total = CartManager.cartItems.sumOf { item ->
                    val product = CartManager.allProducts.find { it.id == item.product_id }
                    (product?.price ?: 0.0) * item.quantity
                }.toInt()
                Toast.makeText(this, "Заказ оформлен на $total ₽", Toast.LENGTH_LONG).show()
                CartManager.cartItems.clear()
                adapter.notifyDataSetChanged()
                updateTotal(totalText)
                finish()
            }
        }
    }

    private fun updateTotal(totalText: TextView) {
        val total = CartManager.cartItems.sumOf { item ->
            val product = CartManager.allProducts.find { it.id == item.product_id }
            (product?.price ?: 0.0) * item.quantity
        }.toInt()
        totalText.text = "Итого: $total ₽"
    }
}
