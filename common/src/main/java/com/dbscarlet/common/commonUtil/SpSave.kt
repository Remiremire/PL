package com.dbscarlet.common.commonUtil

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Daibing Wang on 2018/12/5.
 */
/**
 * SharedPreference管理工具
 */
object SpSave {
    lateinit var sharePreferences: SharedPreferences
    const val SP_NAME = "water_steward"
    const val MODE = Context.MODE_PRIVATE

    inline fun applyEdit(edit: Edit.() -> Unit) {
        val spEditor = sharePreferences.edit()
        val editor = Edit(spEditor)
        edit.invoke(editor)
        spEditor.apply()
    }

    @SuppressLint("ApplySharedPref")
    inline fun commitEdit(edit: Edit.() -> Unit) {
        val spEditor = sharePreferences.edit()
        edit.invoke(Edit(spEditor))
        spEditor.commit()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharePreferences.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharePreferences.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return sharePreferences.getLong(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sharePreferences.getFloat(key, defValue)
    }

    fun getString(key: String, defValue: String?): String? {
        return sharePreferences.getString(key, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>): Set<String>? {
        return sharePreferences.getStringSet(key, defValue)
    }

    fun contain(key: String): Boolean {
        return sharePreferences.contains(key)
    }

    class Edit constructor(
            private val spEditor: SharedPreferences.Editor
    ) {

        fun put(key: String, value: Boolean) {
            spEditor.putBoolean(key, value)
        }

        fun put(key: String, value: Int) {
            spEditor.putInt(key, value)
        }

        fun put(key: String, value: Long) {
            spEditor.putLong(key, value)
        }

        fun put(key: String, value: Float) {
            spEditor.putFloat(key, value)
        }

        fun put(key: String, value: String?) {
            spEditor.putString(key, value)
        }

        fun put(key: String, value: Set<String>?) {
            spEditor.putStringSet(key, value)
        }

        fun remove(key: String) {
            spEditor.remove(key)
        }

        fun clear() {
            spEditor.clear()
        }
    }
}