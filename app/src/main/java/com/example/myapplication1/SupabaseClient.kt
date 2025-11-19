package com.example.myapplication1

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

object SupabaseClientInstance {
    const val SUPABASE_URL = "https://mgklafqwfppkmcawjwuc.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1na2xhZnF3ZnBwa21jYXdqd3VjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM0OTY4ODMsImV4cCI6MjA3OTA3Mjg4M30.ScQU-AIQvmmNZ77tExaWbfLyHYJV-dvvy3fZzbK_Bao"

    private val httpClient = HttpClient(Android)
    private val json = Json { ignoreUnknownKeys = true }

    object client {
        object auth {
            suspend fun signInWith(email: String, password: String) {
                // Mock для работы
            }

            suspend fun signUpWith(email: String, password: String) {
                // Mock для работы
            }

            suspend fun signOut() {
                // Mock для работы
            }

            suspend fun currentUserOrNull(): UserInfo? {
                return UserInfo(email = "user@example.com")
            }
        }

        object postgrest {
            operator fun get(table: String): TableRef {
                return TableRef(table)
            }
        }
    }
}

data class UserInfo(val email: String)

class TableRef(val table: String) {
    fun select(): SelectQuery {
        return SelectQuery(table)
    }
}

class SelectQuery(val table: String) {
    suspend inline fun <reified T> decodeList(): List<T> {
        val httpClient = HttpClient(Android)
        val json = Json { ignoreUnknownKeys = true }

        val url = "${SupabaseClientInstance.SUPABASE_URL}/rest/v1/$table?select=*"

        val response: HttpResponse = httpClient.get(url) {
            headers {
                append("apikey", SupabaseClientInstance.SUPABASE_ANON_KEY)
                append("Authorization", "Bearer ${SupabaseClientInstance.SUPABASE_ANON_KEY}")
            }
        }

        val responseText = response.bodyAsText()
        httpClient.close()

        return json.decodeFromString<List<T>>(responseText)
    }
}
