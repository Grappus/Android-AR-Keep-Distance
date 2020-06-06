package com.grappus.covidar.android.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Dipanshu Harbola on 4/6/20.
 */
object Prefs {

    val PREF_NAME = "covid_ar"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
    }

    fun clear() {
        prefs!!.edit().clear().apply()
    }

    fun getPrefsString(key: String): String {
        return prefs?.getString(key, "") ?: ""
    }

    fun getPrefsBoolean(key: String): Boolean {
        return prefs!!.getBoolean(key, false)
    }

    fun getPrefsBooleanDefault(key: String, defaultVal: Boolean): Boolean {
        return prefs!!.getBoolean(key, defaultVal)
    }

    fun getPrefsInt(key: String): Int {
        return prefs!!.getInt(key, 0)
    }

    fun getPrefsLong(key: String): Long {
        return prefs!!.getLong(key, 0)
    }

    fun getPrefsFloat(key: String): Float {
        return prefs!!.getFloat(key, 0f)
    }

    fun getPrefsFloat(key: String, defaultValue: Float?): Float {
        return prefs!!.getFloat(key, defaultValue!!)
    }

    fun setPrefs(key: String, value: String) {
        prefs!!.edit().putString(key, value).apply()
    }

    fun setPrefs(key: String, value: Boolean) {
        prefs!!.edit().putBoolean(key, value).apply()
    }

    fun setPrefs(key: String, value: Int) {
        prefs!!.edit().putInt(key, value).apply()
    }

    fun setPrefs(key: String, value: Long) {
        prefs!!.edit().putLong(key, value).apply()
    }

    fun setPrefs(key: String, value: Float) {
        prefs!!.edit().putFloat(key, value).apply()
    }

    fun removePref(key: String) {
        prefs!!.edit().remove(key).apply()
    }
}