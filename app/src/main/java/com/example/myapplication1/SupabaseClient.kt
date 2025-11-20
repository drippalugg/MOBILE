package com.example.myapplication1

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClientInstance {
    const val SUPABASE_URL = "https://mgklafqwfppkmcawjwuc.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1na2xhZnF3ZnBwa21jYXdqd3VjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM0OTY4ODMsImV4cCI6MjA3OTA3Mjg4M30.ScQU-AIQvmmNZ77tExaWbfLyHYJV-dvvy3fZzbK_Bao"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth) {
            autoRefreshToken = true
            autoLoadUser = true
        }
        install(Postgrest)
        install(Realtime)
    }
}

// Вспомогательные импорты для Короутин
// Подробное объяснение:
// Если SupabaseClient введение signInWith(Email) в LoginActivity выдает ошибки,
// это может быть три причины:
//
// 1. Это построение DSL supabase-kt 1.4.0 требует определения значений
//    внутри котекста Email() { }
//
// 2. В kotlinя, при использовании signInWith(Email), НЕ используйте обычные скобки
//    для аргументов. Корректный синтаксис:
//    client.auth.signInWith(Email) { email = "..."; password = "..." }
//
// 3. Если build.gradle не содержит зависимости supabase-kt (выще указаны),
//    то ДОБАВИТЕ их в dependencies {
//    implementationio.github.jan-tennertauth-kt1.4.0
//    implementationio.github.jan-tennertpostgrest-kt1.4.0
//    implementationio.github.jan-tennertrealtime-kt1.4.0
