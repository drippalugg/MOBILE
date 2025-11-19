package com.example.myapplication1

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://mgklafqwfppkmcawjwuc.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1na2xhZnF3ZnBwa21jYXdqd3VjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM0OTY4ODMsImV4cCI6MjA3OTA3Mjg4M30.ScQU-AIQvmmNZ77tExaWbfLyHYJV-dvvy3fZzbK_Bao"
    ) {
        install(Auth)
        install(Postgrest)
    }
}
