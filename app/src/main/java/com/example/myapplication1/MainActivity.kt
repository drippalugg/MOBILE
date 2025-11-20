package com.example.myapplication1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var categoriesContainer: LinearLayout
    private lateinit var productsList: ListView
    private lateinit var searchInput: EditText
    private lateinit var profileBtn: Button
    private lateinit var cartBtn: Button
    private lateinit var productsAdapter: ProductAdapter
    private val categories = mutableListOf<Category>()
    private val filteredProducts = mutableListOf<Product>()
    private var currentCategoryId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем сессию
        if (!SessionManager.isSessionActive(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Инициализируем views
        categoriesContainer = findViewById(R.id.categoriesContainer)
        productsList = findViewById(R.id.productsList)
        searchInput = findViewById(R.id.searchInput)
        profileBtn = findViewById(R.id.profileBtn)
        cartBtn = findViewById(R.id.cartBtn)

        // Загружаем корзину из хранилища
        CartManager.loadFromStorage(this)

        productsAdapter = ProductAdapter(this, filteredProducts) { product ->
            val existingItem = CartManager.cartItems.find { it.product_id == product.id }
            if (existingItem != null) {
                existingItem.quantity++
            } else {
                CartManager.cartItems.add(
                    CartItem(
                        id = CartManager.cartItems.size.toLong(),
                        product_id = product.id,
                        quantity = 1
                    )
                )
            }
            // Сохраняем корзину
            CartManager.saveToStorage(this)

            Toast.makeText(this, "${product.name} добавлен в корзину ", Toast.LENGTH_SHORT).show()
            productsList.adapter = productsAdapter
        }

        productsList.adapter = productsAdapter

        // Поиск
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateFilteredProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Кнопки навигации
        profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Загружаем данные
        loadCategories()
        loadProducts()
    }

    override fun onPause() {
        super.onPause()
        // Сохраняем корзину при выходе из Activity
        CartManager.saveToStorage(this)
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            try {
                val categoryList = SupabaseClientInstance.client
                    .postgrest["categories"]
                    .select()
                    .decodeList<Category>()

                categories.clear()
                categories.addAll(categoryList)
                displayCategories()


            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Ошибка загрузки категорий: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun displayCategories() {
        categoriesContainer.removeAllViews()

        // Кнопка "Все"
        val allButton = Button(this).apply {
            text = "Все"
            setBackgroundColor(if (currentCategoryId == null) Color.parseColor("#2196F3") else Color.parseColor("#CCCCCC"))
            setTextColor(Color.WHITE)
            setPadding(24, 12, 24, 12)
            setOnClickListener {
                currentCategoryId = null
                displayCategories()
                updateFilteredProducts()
            }
        }
        categoriesContainer.addView(allButton)

        // Кнопки категорий
        categories.forEach { category ->
            val button = Button(this).apply {
                text = category.name
                setBackgroundColor(if (currentCategoryId == category.id) Color.parseColor("#2196F3") else Color.parseColor("#CCCCCC"))
                setTextColor(Color.WHITE)
                setPadding(24, 12, 24, 12)
                setOnClickListener {
                    currentCategoryId = category.id
                    displayCategories()
                    updateFilteredProducts()
                }
            }
            categoriesContainer.addView(button)
        }
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                val productList = SupabaseClientInstance.client
                    .postgrest["products"]
                    .select()
                    .decodeList<Product>()

                CartManager.allProducts.clear()
                CartManager.allProducts.addAll(productList)
                updateFilteredProducts()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Ошибка загрузки товаров: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateFilteredProducts(query: String = "") {
        val filtered = CartManager.allProducts.filter { product ->
            val matchesCategory = currentCategoryId?.let { product.category_id == it } ?: true
            val matchesSearch = product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesSearch
        }

        filteredProducts.clear()
        filteredProducts.addAll(filtered)
        productsAdapter.notifyDataSetChanged()
    }
}