package com.example.myapplication1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailInput: EditText = findViewById(R.id.emailInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val confirmPasswordInput: EditText = findViewById(R.id.confirmPasswordInput)
        val signUpBtn: Button = findViewById(R.id.signUpBtn)
        val loginLink: TextView = findViewById(R.id.loginLink)

        signUpBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Пароль должен быть минимум 6 символов", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                try {
                    SupabaseClient.client.auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    Toast.makeText(this@SignUpActivity, "Регистрация успешна! Проверьте почту.", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this@SignUpActivity, e.message ?: "Ошибка регистрации", Toast.LENGTH_LONG).show()
                }
            }
        }

        loginLink.setOnClickListener {
            finish()
        }
    }
}
