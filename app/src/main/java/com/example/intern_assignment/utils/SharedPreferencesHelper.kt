package com.example.intern_assignment.utils

import android.content.Context

class SharedPreferenceHelper(private val context: Context) {

    companion object{
        private const val MY_PREF_KEY = "MY_PREF"
        private const val IS_BUYER= "IS_BUYER"
    }

    fun saveStringData(key: String,data: String?) {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key,data).apply()
    }
 fun saveBooleanData(key: String,data: Boolean) {
        val sharedPreferences = context.getSharedPreferences(IS_BUYER, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(key,data).apply()
    }
    fun getStringData(key: String): String? {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }
 fun getBooleanData(key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(IS_BUYER, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, true)
    }
    fun clearPreferences() {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        val sharedPreferencesBuyer = context.getSharedPreferences(IS_BUYER, Context.MODE_PRIVATE)
         sharedPreferences.edit().clear().apply()
        sharedPreferencesBuyer.edit().clear().apply()

    }
}