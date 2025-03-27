package com.lambao.base.data.pref

interface PreferenceRepository {
    suspend fun setInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int

    suspend fun setLong(key: String, value: Long)
    fun getLong(key: String, defaultValue: Long = 0L): Long

    suspend fun setFloat(key: String, value: Float)
    fun getFloat(key: String, defaultValue: Float = 0f): Float

    suspend fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    suspend fun setString(key: String, value: String)
    fun getString(key: String, defaultValue: String = ""): String?

    suspend fun removeKey(key: String)
    fun clear()
}