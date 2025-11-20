package com.example.myapplication1

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ProductAdapter(
    private val context: Context,
    private val products: List<Product>,
    private val onBuyClick: (Product) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Any = products[position]

    override fun getItemId(position: Int): Long = products[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = products[position]

        val linearLayout = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(15, 15, 15, 15)
            setBackgroundColor(Color.WHITE)
        }

        val titleLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val nameView = TextView(context).apply {
            text = product.name
            textSize = 16f
            setTextColor(Color.parseColor("#333333"))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val statusView = TextView(context).apply {
            text = if (product.in_stock) "✓ В наличии" else "✗ Нет в наличии"
            textSize = 12f
            setTextColor(
                Color.parseColor(
                    if (product.in_stock) "#28A745" else "#DC3545"
                )
            )
        }

        titleLayout.addView(nameView)
        titleLayout.addView(statusView)

        val priceLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 10, 0, 0)
        }

        val priceView = TextView(context).apply {
            text = "${product.price.toInt()} ₽"
            textSize = 14f
            setTextColor(Color.parseColor("#F39C12"))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val buyBtn = Button(context).apply {
            text = "В корзину"
            textSize = 12f
            isEnabled = product.in_stock  // И ТУТ ТОЖЕ!
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#667EEA"))
            setPadding(10, 5, 10, 5)
            setOnClickListener { onBuyClick(product) }
            layoutParams = LinearLayout.LayoutParams(
                150,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        priceLayout.addView(priceView)
        priceLayout.addView(buyBtn)

        linearLayout.addView(titleLayout)
        linearLayout.addView(priceLayout)

        return linearLayout
    }
}
