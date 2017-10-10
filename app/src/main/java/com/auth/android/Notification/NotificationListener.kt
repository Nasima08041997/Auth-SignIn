package com.auth.android.Notification

/**
 * Created by nasima on 09/10/17.
 */
interface NotificationListener {

    fun sendReceiverId(receiverId:String)
    fun receiveReceiverId():String
}