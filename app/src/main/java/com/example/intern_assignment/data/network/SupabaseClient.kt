package com.example.intern_assignment.data.network


import com.example.intern_assignment.utils.Constants
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = Constants.supabaseUrl,
        supabaseKey = Constants.supabaseKey
    ) {
        install(Auth)
        install(Storage)
        install(Postgrest)
        //install other modules
    }
}