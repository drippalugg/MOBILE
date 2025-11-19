package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val emailText = findViewById<TextView>(R.id.emailText)
        val firstNameInput = findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = findViewById<EditText>(R.id.lastNameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val backBtn = findViewById<Button>(R.id.backProfileBtn)

        lifecycleScope.launch {
            try {
                val user = SupabaseClientInstance.client.auth.currentUserOrNull()
                emailText.text = "Email: ${user?.email ?: "Не авторизован"}"
            } catch (e: Exception) {
                emailText.text = "Ошибка загрузки"
            }
        }

        saveBtn.setOnClickListener {
            val firstName = firstNameInput.text.toString()
            val lastName = lastNameInput.text.toString()
            val address = addressInput.text.toString()

            if (firstName.isNotEmpty() || lastName.isNotEmpty() || address.isNotEmpty()) {
                Toast.makeText(
                    this,
                    "Данные сохранены!\nИмя: $firstName\nФамилия: $lastName\nАдрес: $address",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Заполните хотя бы одно поле", Toast.LENGTH_SHORT).show()
            }
        }

        logoutBtn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    SupabaseClientInstance.client.auth.signOut()
                    Toast.makeText(this@ProfileActivity, "Вы вышли", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        backBtn.setOnClickListener {
            finish()
        }
    }
}
