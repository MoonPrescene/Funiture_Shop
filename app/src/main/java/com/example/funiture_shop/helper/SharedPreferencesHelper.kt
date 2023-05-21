package com.example.funiture_shop.helper

import android.content.Context
import com.example.funiture_shop.common.Const
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesHelper @Inject constructor(@ApplicationContext context: Context) {
    private val prefs =
        context.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun put(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun get(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue)!!
    }

    fun get(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun get(key: String, defaultValue: Float): Float {
        return prefs.getFloat(key, defaultValue)
    }

    fun get(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun getUserName(): String {
        return get(USER_NAME, "")
    }

    fun saveUserInfo(userName: String) {
        put(USER_NAME, userName)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        const val USER_NAME = "USER_CODE"
    }
}