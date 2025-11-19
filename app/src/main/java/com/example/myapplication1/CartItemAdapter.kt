package com.example.myapplication1

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class CartItemAdapter(
    private val context: Context,
    private val cartItems: List<CartItem>,
    private val allProducts: List<Product>,
    private val onQuantityChanged: () -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = cartItems.size
    override fun getItem(position: Int): Any = cartItems[position]
    override fun getItemId(position: Int): Long = cartItems[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cartItem = cartItems[position]
        val product = allProducts.find { it.id == cartItem.product_id }

        val linearLayout = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(15, 15, 15, 15)
            setBackgroundColor(Color.WHITE)
        }

        val nameView = TextView(context).apply {
            text = "${product?.name ?: "Товар"}"
            textSize = 16f
            setTextColor(Color.parseColor("#333333"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val priceLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 10, 0, 0)
        }

        val priceView = TextView(context).apply {
            text = "${(product?.price ?: 0.0).toInt()} ₽ x ${cartItem.quantity}"
            textSize = 14f
            setTextColor(Color.parseColor("#F39C12"))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val minusBtn = Button(context).apply {
            text = "−"
            textSize = 12f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#FF6B6B"))
            setPadding(5, 5, 5, 5)
            setOnClickListener {
                if (cartItem.quantity > 1) {
                    CartManager.cartItems[position] = CartItem(
                        cartItem.id,
                        cartItem.product_id,
                        cartItem.quantity - 1
                    )
                }
                onQuantityChanged()
            }
            layoutParams = LinearLayout.LayoutParams(40, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        val deleteBtn = Button(context).apply {
            text = "✕"
            textSize = 12f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#DC3545"))
            setPadding(5, 5, 5, 5)
            setOnClickListener {
                CartManager.cartItems.removeAt(position)
                onQuantityChanged()
            }
            layoutParams = LinearLayout.LayoutParams(40, LinearLayout.LayoutParams.WRAP_CONTENT)
        }

        priceLayout.addView(priceView)
        priceLayout.addView(minusBtn)
        priceLayout.addView(deleteBtn)

        linearLayout.addView(nameView)
        linearLayout.addView(priceLayout)

        return linearLayout
    }
}
