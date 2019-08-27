package com.jdy.android.fortube.base

import android.content.SharedPreferences

class PrefHelper(private val mPref: SharedPreferences) {
    companion object {
        const val PREF_KEY_LOCATION_PERMISSION = "PREF_KEY_LOCATION_PERMISSION"
    }

    fun denyShowPermissionPopup() {
        mPref.edit().apply {
            putBoolean(PREF_KEY_LOCATION_PERMISSION, true)
        }.apply()
    }

    fun isDeniedShowPermissionPopup(): Boolean {
        return mPref.getBoolean(PREF_KEY_LOCATION_PERMISSION, false)
    }
}