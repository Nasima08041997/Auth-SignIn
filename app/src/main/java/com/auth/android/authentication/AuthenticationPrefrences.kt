package com.auth.android.authentication

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.twitter.sdk.android.core.models.User

/**
 * Created by nasima on 05/10/17.
 */
class AuthenticationPreference(mContext: Context) {

    private val mSharedPreferences: SharedPreferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)

    fun setUser(user: User) {
        getFirebaseDatabase().child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
        mSharedPreferences.edit().putString(User::class.java.simpleName, Gson().toJson(user)).apply()
    }

    fun getFirebaseDatabase(): DatabaseReference {
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        return mDatabaseRef
    }

    fun getUser(): User? {
        val userDetails = mSharedPreferences.getString(User::class.java.simpleName, "")
        if (userDetails.isEmpty()) {
            return null
        } else {
            return Gson().fromJson(userDetails, User::class.java)
        }
    }
}