package com.lambao.base.data.pref

import android.content.SharedPreferences

class PreferenceRepositoryImpl(
    private val pref: SharedPreferences,
    private val editor: SharedPreferences.Editor,
) : PreferenceRepository {
    override suspend fun setInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return pref.getInt(key, defaultValue)
    }

    override suspend fun setLong(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return pref.getLong(key, defaultValue)
    }

    override suspend fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return pref.getFloat(key, defaultValue)
    }

    override suspend fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    override suspend fun setString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String): String? {
        return pref.getString(key, defaultValue)
    }

    override suspend fun removeKey(key: String) {
        editor.remove(key).apply()
    }

    override fun clear() {
        editor.clear().apply()
    }
}