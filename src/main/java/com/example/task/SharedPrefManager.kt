package com.example.task

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SharedPrefManager(context: Context) {

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    fun securePreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "secure_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveString(context: Context, key: String, value: String) {
        Log.d("SharedPref", "saveString: "+key +"Value :"+value)
        val sharedPreferences = securePreferences(context)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getPASSWORD(context: Context, key: String, defaultValue: String): String? {
        val sharedPreferences = securePreferences(context)
        return sharedPreferences.getString(key, defaultValue)
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "PinLoginPref"
        private const val KEY_PIN = "pin"
    }

    fun getPin(): String {
        return sharedPreferences.getString(KEY_PIN, "") ?: ""
    }

    fun setPin(pin: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PIN, pin)
        editor.apply()
    }
}
