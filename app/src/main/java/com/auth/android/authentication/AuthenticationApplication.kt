package com.auth.android.authentication

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.storage.UploadTask
import com.twitter.sdk.android.core.Twitter

/**
 * Created by nasima on 05/10/17.
 */
class AuthenticationApplication : Application(){

    lateinit var uploadTasks: HashMap<String, UploadTask>

    override fun onCreate() {
        super.onCreate()
        instance = this
        uploadTasks = HashMap()
//        FacebookSdk.sdkInitialize(applicationContext)
       // AppEventsLogger.activateApp(this)
        Twitter.initialize(this)
    }

    companion object {

        var AuthenticationPreference : AuthenticationPreference ? = null
        fun getPreferences() : AuthenticationPreference {
            if( AuthenticationPreference == null ) AuthenticationPreference =AuthenticationPreference(instance)
            return AuthenticationPreference as AuthenticationPreference
        }

        lateinit var instance: AuthenticationApplication
            private set
    }

}