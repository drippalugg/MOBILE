package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backBtn = findViewById<Button>(R.id.backProfileBtn)
        val emailText = findViewById<TextView>(R.id.emailText)
        val firstNameInput = findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = findViewById<EditText>(R.id.lastNameInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        // Показываем email из сессии
        val userEmail = SessionManager.getUserEmail(this)
        emailText.text = "Email: $userEmail"

        // Загружаем сохранённые данные профиля
        loadProfileData(firstNameInput, lastNameInput, addressInput)

        backBtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val address = addressInput.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Сохраняем профиль
            ProfileDataManager.saveProfile(this, firstName, lastName, address)

            Toast.makeText(
                this,
                "✅ Профиль сохранён: $firstName $lastName",
                Toast.LENGTH_SHORT
            ).show()
        }

        logoutBtn.setOnClickListener {
            // Очищаем сессию и профиль
            SessionManager.clearSession(this)
            ProfileDataManager.clearProfile(this)

            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()

            // Переходим на экран входа
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfileData(
        firstNameInput: EditText,
        lastNameInput: EditText,
        addressInput: EditText
    ) {
        firstNameInput.setText(ProfileDataManager.getFirstName(this))
        lastNameInput.setText(ProfileDataManager.getLastName(this))
        addressInput.setText(ProfileDataManager.getAddress(this))
    }
}