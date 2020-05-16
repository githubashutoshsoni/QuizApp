package com.example.quizapp

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

object PrefHelper {

    fun helo() {

        println("Static util class like java in kotlin")
    }

    val TAG = PrefHelper::class.simpleName

    fun setUpSHaredPreference(user: String, activity: Activity) {

        Log.d(TAG, "set up shared preferences success $user ")

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(activity.getString(com.example.quizapp.R.string.user_name), user)
            commit()
        }


    }


    fun getUserDetails(activity: Activity): String? {

        val userName = activity.getPreferences(Context.MODE_PRIVATE)
            ?.getString(activity.getString(R.string.user_name), null);

        return userName
    }


    fun clearLoginDetails(activity: Activity) {

        FirebaseAuth.getInstance().signOut()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(activity.getString(com.example.quizapp.R.string.user_name), null)
            commit()
        }
    }

}