package com.appsng.firebaseapptoappnotification.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue

import java.util.HashMap

/**
 * Created by Akinsete on 7/30/16.
 */


class NotificationModel() {

    lateinit var sender_id: String
    lateinit var receiver_id: String
    lateinit var message: String
    lateinit var description: String
    lateinit var type: String
    var timestamp: Long = 0
    var status: Long = 0

}