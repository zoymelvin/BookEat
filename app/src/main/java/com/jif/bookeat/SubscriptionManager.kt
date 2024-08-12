package com.jif.bookeat

import android.content.Context
import android.content.SharedPreferences

class SubscriptionManager(private val context: Context) {

    companion object {
        const val PREF_NAME = "SubscriptionPreferences"
        const val KEY_SUBSCRIBED = "subscribed"
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isSubscribed(): Boolean {
        return preferences.getBoolean(KEY_SUBSCRIBED, false)
    }

    fun subscribe() {
        preferences.edit().putBoolean(KEY_SUBSCRIBED, true).apply()
    }

    fun unsubscribe() {
        preferences.edit().putBoolean(KEY_SUBSCRIBED, false).apply()
    }
}