package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var emailText: TextView
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        emailText = findViewById(R.id.emailText)
        logoutBtn = findViewById(R.id.logoutBtn)

        loadProfile()

        logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()
                if (user != null) {
                    emailText.text = "Email: ${user.email}"
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signOut()
                Toast.makeText(this@ProfileActivity, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Ошибка выхода", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
