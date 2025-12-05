package dataLayer.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

fun storeToken(context: Context, token: String){
    val preferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    preferences.edit { putString("api_token", token) }
}

fun  getToken(context: Context): String?{
    val preferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    return preferences.getString("api_token", null)
}

fun removeToken(context: Context){
    val preferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    preferences.edit { remove("api_token") }
}