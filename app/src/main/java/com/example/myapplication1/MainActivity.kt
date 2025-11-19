package com.example.myapplication1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val inStock: Boolean
)

class MainActivity : AppCompatActivity() {

    private val allProducts = mutableListOf(
        Product(1, "Фильтр воздушный", 450.0, true),
        Product(2, "Масло моторное 5W-30", 850.0, true),
        Product(3, "Прокладка ГБЦ", 1200.0, false),
        Product(4, "Замок зажигания", 2500.0, true),
        Product(5, "Тормозные колодки", 1800.0, true),
        Product(6, "Свечи зажигания комплект", 650.0, true),
        Product(7, "Ремень ГРМ", 3200.0, false),
        Product(8, "Фильтр топливный", 550.0, true),
        Product(9, "Патрубок радиатора", 750.0, true),
        Product(10, "Генератор", 5500.0, true)
    )

    private val cart = mutableListOf<Product>()
    private lateinit var productsList: ListView
    private lateinit var searchInput: EditText
    private lateinit var cartCount: TextView
    private lateinit var totalPrice: TextView
    private lateinit var checkoutBtn: Button
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productsList = findViewById(R.id.productsList)
        searchInput = findViewById(R.id.searchInput)
        cartCount = findViewById(R.id.cartCount)
        totalPrice = findViewById(R.id.totalPrice)
        checkoutBtn = findViewById(R.id.checkoutBtn)

        displayProducts(allProducts)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        checkoutBtn.setOnClickListener {
            if (cart.isEmpty()) {
                Toast.makeText(this, "Корзина пуста!", Toast.LENGTH_SHORT).show()
            } else {
                val total = cart.sumOf { it.price }.toInt()
                Toast.makeText(
                    this,
                    "Заказ оформлен! Итого: $total ₽",
                    Toast.LENGTH_LONG
                ).show()
                cart.clear()
                updateCartUI()
            }
        }
    }

    private fun displayProducts(items: List<Product>) {
        adapter = ProductAdapter(this, items) { product ->
            if (product.inStock) {
                addToCart(product)
            } else {
                Toast.makeText(this, "Товар недоступен", Toast.LENGTH_SHORT).show()
            }
        }
        productsList.adapter = adapter
    }

    private fun filterProducts(query: String) {
        val filtered = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true)
            }
        }
        displayProducts(filtered)
    }

    private fun addToCart(product: Product) {
        cart.add(product)
        updateCartUI()
        Toast.makeText(
            this,
            "${product.name} добавлен в корзину",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateCartUI() {
        cartCount.text = cart.size.toString()
        val total = cart.sumOf { it.price }.toInt()
        totalPrice.text = "$total ₽"
    }
}
