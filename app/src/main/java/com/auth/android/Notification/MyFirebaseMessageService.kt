package com.auth.android.Notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.auth.android.R
import com.auth.android.authentication.SplashNavigationListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.text.Html
import android.os.IBinder

import com.appsng.firebaseapptoappnotification.models.NotificationModel
import com.fitlinks.chat.model.MessageModel
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase





/**
 * Created by nasima on 09/10/17.
 */
class MyFirebaseMessageService :  Service() {
    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    lateinit var mDatabase: FirebaseDatabase
        lateinit var firebaseAuth: FirebaseAuth
        lateinit var context: Context
        lateinit var receiverIdListener : NotificationListener
        //var   receiverId =  "+918508439991"  //emulatlor sender - jio
                                            //mobile sender - aircel
        //var   senderId ="+918248947745"

   var receiverId =  "+918248947745"  //emulatlor sender - jio
    //mobile sender - aircel
   var senderId ="+918508439991"


       override fun onCreate() {
            super.onCreate()
            context = this
            mDatabase = FirebaseDatabase.getInstance()
            firebaseAuth = FirebaseAuth.getInstance()

            setupNotificationListener()
       }


    private fun setupNotificationListener() {
            mDatabase.reference.child("Notifications")
                    .child(senderId)
                    .orderByChild("status").equalTo(0.0)
                    .addChildEventListener(object : ChildEventListener {
                        override fun onCancelled(p0: DatabaseError?) {
                        }

                        override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onChildRemoved(p0: DataSnapshot?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onChildAdded(dataSnapshot: DataSnapshot?, s: String?) {
                            if (dataSnapshot != null) {
                                val notification = dataSnapshot.getValue<NotificationModel>(NotificationModel::class.java)
                                showNotification(context, notification, dataSnapshot.key)
                            }
                        }
                    })


       }


    override fun onDestroy() {
            super.onDestroy()
    }

    fun showNotification(context: Context, notification: NotificationModel?, notification_key: String) {
         val intent = Intent(this, MyFirebaseMessageService::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                 PendingIntent.FLAG_ONE_SHOT)
         val channelId = getString(R.string.default_notification_channel_id)
         val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
         val notificationBuilder = NotificationCompat.Builder(context,notification!!.receiver_id)
                 .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle(receiverId)
                 .setContentText(notification!!.message)
                 .setAutoCancel(true)
                 .setSound(defaultSoundUri)
                 .setContentIntent(pendingIntent)
         val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

            /* Update firebase set notifcation with this key to 1 so it doesnt get pulled by our notification listener*/
            flagNotificationAsSent(notification_key)
        }

    fun flagNotificationAsSent(notification_key: String) {
            mDatabase.reference.child("notifications")
                    .child(senderId)
                    .child(notification_key)
                    .child("status")
                    .setValue(1)
    }

        companion object {
            internal var TAG = "FirebaseService"
        }

}


