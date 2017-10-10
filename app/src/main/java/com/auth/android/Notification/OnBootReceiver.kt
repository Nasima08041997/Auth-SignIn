package com.auth.android.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent



class OnBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startService(Intent(context, MyFirebaseMessageService::class.java))
    }
}
