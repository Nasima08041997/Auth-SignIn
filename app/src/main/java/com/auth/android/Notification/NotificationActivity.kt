package com.auth.android.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.auth.android.R
import com.auth.android.authentication.AuthPresenter
import com.auth.android.authentication.AuthenticationApplication
import com.auth.android.authentication.AuthenticationPreference
import com.auth.android.authentication.SignUpSignInFragment
import com.fitlinks.authentication.AuthView
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_notification.*

/**
 * Created by nasima on 09/10/17.
 */
class NotificationActivity : AppCompatActivity(),AuthView  {


    lateinit var authPresenter : AuthPresenter

    private val TAG = "NotificationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService<NotificationManager>(NotificationManager::class.java)
            notificationManager.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }



        if (intent.extras != null) {
            for (key in intent.extras.keySet()) {
                val value = intent.extras.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }



        val token = FirebaseInstanceId.getInstance().token //device registration token
        Log.d(TAG, "device registration token"+token)
        Toast.makeText(this@NotificationActivity, token, Toast.LENGTH_SHORT).show()


        FirebaseMessaging.getInstance().subscribeToTopic("Notification")
        val msg = getString(R.string.notification_subscribed)
        Log.d(TAG, msg)
        Toast.makeText(this@NotificationActivity, msg, Toast.LENGTH_SHORT).show()


        btnSendNotification.setOnClickListener {
                        //sendNotification()
        }




    }
    //override fun sendNotification() {
        var user = AuthenticationApplication.getPreferences().getUser()
        val message = etMessage.getText().toString()
        /*val intent = Intent(this, NotificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Fit_links")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
*/

//    }

    override fun showProgress(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUI(user: FirebaseUser?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}