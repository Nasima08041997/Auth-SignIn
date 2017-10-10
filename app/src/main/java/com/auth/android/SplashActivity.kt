package com.auth.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.auth.android.authentication.*
import kotlinx.android.synthetic.main.fragment_otp.*
import com.tbruyelle.rxpermissions2.RxPermissions
import android.support.v4.app.ActivityCompat.requestPermissions
import android.Manifest.permission.SEND_SMS
import android.os.Build
import android.Manifest.permission.SEND_SMS
import android.content.pm.PackageManager
import com.auth.android.Notification.NotificationActivity
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.EditText
import com.auth.android.Notification.ChatListActivity
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() , SplashNavigationListener {


    //    var rxPermissions = RxPermissions(this) // where this is an Activity instance
    lateinit var authPresenter : AuthPresenter
    lateinit var dataModel : DataModel
    lateinit var phone : String
    lateinit var messageOTP :String
    lateinit var message:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //setOTP("223456")
        phonenumberFragment()
    }

    lateinit var signupSigninFragment: SignUpSignInFragment

    override fun loadSignupFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        signupSigninFragment = SignUpSignInFragment()
        fragmentTransaction.replace(R.id.container, signupSigninFragment).commit()    }

    lateinit var OtpFragment: OtpFragment
    override fun loadOtpFragment() {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        OtpFragment = OtpFragment()
        fragmentTransaction.replace(R.id.container, OtpFragment).commit()
    }



    lateinit var PhonenumberFragment: PhonenumberFragment
    override fun phonenumberFragment() {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
                        PhonenumberFragment =PhonenumberFragment()
        fragmentTransaction.replace(R.id.container, PhonenumberFragment).commit()
    }

    lateinit var welcomeFragment: WelcomeFragment
    override fun loadWelcomeFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        welcomeFragment = WelcomeFragment()
        fragmentTransaction.replace(R.id.container, welcomeFragment).commit()
    }


    lateinit var EmailFragment: EmailFragment
    override fun loademailFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        EmailFragment = EmailFragment()
        fragmentTransaction.replace(R.id.container, EmailFragment).commit()
    }

    override fun loadNotification() {
        val intent = Intent(this, ChatListActivity::class.java)
        startActivity(intent)
    }


  override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("otp"))
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.equals("otp", ignoreCase = true)) {
                message = intent.getStringExtra("message")
                setOTP(message)
                OtpFragment.otpListener()
               // messageOTP=getOTP()
                Log.i("message-inside activity",message)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (signupSigninFragment != null) {
            signupSigninFragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setauthPresenter(authPresenter: AuthPresenter) {
    this.authPresenter=authPresenter
    }
    override fun getauthPresenter(): AuthPresenter {
        return authPresenter
    }



    override fun loginData(dataModel: DataModel) {
        this.dataModel = dataModel
    }

    override fun getData(): DataModel {
        return dataModel
    }


    override fun setPhoneNumber(phonenumber: String) {

        this.phone = phonenumber
        Log.i("phone number--------->",phone)

    }

    override fun getPhoneNumber(): String {
        return phone
    }

    override fun setOTP(message: String) {
        this.messageOTP = message
        Log.i("set message",message)

    }

    override fun getOTP(): String {
        return this.messageOTP
        Log.i("get message",messageOTP)


    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

}
