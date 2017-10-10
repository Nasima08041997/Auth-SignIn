package com.auth.android.authentication

import android.Manifest
import android.Manifest.permission.SEND_SMS
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.PaintDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast
import com.auth.android.BroadcastSms
import com.auth.android.R
import com.auth.android.R.id.*
import com.auth.android.SplashActivity


import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tbruyelle.rxpermissions2.RxPermissionsFragment
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.view.*
import kotlinx.android.synthetic.main.fragment_welcome.*
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS


/**
 * Created by nasima on 05/10/17.
 */
class OtpFragment : Fragment() , View.OnClickListener  {

    lateinit var splashNavigationListener: SplashNavigationListener
    lateinit var phonenumber: String
    private val mAuthe: FirebaseAuth? = null
    lateinit var Otp: String
    lateinit var message:String

    companion object {

        private val TAG = "PhoneAuthActivity"

        private val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"

        private val STATE_INITIALIZED = 1
        private val STATE_CODE_SENT = 2
        private val STATE_VERIFY_FAILED = 3
        private val STATE_VERIFY_SUCCESS = 4
        private val STATE_SIGNIN_FAILED = 5
        private val STATE_SIGNIN_SUCCESS = 6
    }



    fun getFirebaseAuth(): FirebaseAuth {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth
    }

    val mAuth = getFirebaseAuth()

    fun getPhoneAuth(): PhoneAuthProvider{
        val phoneAuth = PhoneAuthProvider.getInstance()
        return phoneAuth
    }

    private var mVerificationInProgress = false
    lateinit var mVerificationId: String
    var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    lateinit var  mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_otp, container, false)
        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashNavigationListener) {
            splashNavigationListener = context
        }

    }

    @SuppressLint("StringFormatMatches")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phonenumber = splashNavigationListener.getPhoneNumber()
        if(phonenumber.isNotEmpty()) {
            PhoneNumberVerification(phonenumber)
            // sendSMS(phonenumber,"123456 is your verification code")
           // message = splashNavigationListener.getOTP()


        }
       // val res = resources
        //res.getString(R.string.otp_info,phonenumber)
        btnSubmit.setOnClickListener(this)
        tvResendOtp.setOnClickListener(this)


    }

    fun otpListener()
    {
        message = splashNavigationListener.getOTP()
        var one = message.toCharArray()[0]
        etOtpStart.setText(message.toCharArray(),0,1)
        etOtpOne.setText(message.toCharArray(),1,1)
        etOtpTwo.setText(message.toCharArray(),2,1)
        etOtpThree.setText(message.toCharArray(),3,1)
        etOtpFour.setText(message.toCharArray(),4,1)
        etOtpEnd.setText(message.toCharArray(),5,1)






    }


    fun sendSMS(phoneNumber: String, message: String) {

        val pi = getActivity(activity, 0,
                Intent(activity, Manifest.permission_group.SMS::class.java), 0)
        val sms = SmsManager.getDefault()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("permission", "permission denied to SEND_SMS - requesting it")
            val permissions = arrayOf<String>(Manifest.permission.SEND_SMS)
                    requestPermissions(permissions, 112)
                    sms.sendTextMessage(phoneNumber, null, message, pi, null)
        }

    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnSubmit -> {

                verifyPhoneNumberWithCode(mVerificationId,etOtpOne.text.toString())
            }

            R.id.tvResendOtp -> {

                    resendVerificationCode(phonenumber,mResendToken)

            }
        }
    }


    private fun validatePhoneNumber(): Boolean {
        if ((phonenumber).isNullOrEmpty()) {
            return false
        }
        return true
    }





    fun PhoneNumberVerification(phonenumber: String) {
        val phoneAuth = getPhoneAuth()
        phoneAuth.verifyPhoneNumber(
                phonenumber,
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity, // Activity (for callback binding)
                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        mVerificationInProgress = false
                       signInWithPhoneAuthCredential(credential)


                    }

                    override fun onVerificationFailed(exception: FirebaseException?) {

                        mVerificationInProgress = false
                        if (exception is FirebaseAuthInvalidCredentialsException) {

                        } else if (exception is FirebaseTooManyRequestsException) {

                        }

                    }

                    override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                        super.onCodeSent(verificationId, token)
                        Log.d(TAG, "onCodeSent:" + verificationId);
                        mVerificationId = verificationId!!
                        mResendToken = token


                    }

                } )
        mVerificationInProgress = true
    }


    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(phoneNumber: String,
                                       token: PhoneAuthProvider.ForceResendingToken?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity, // Activity (for callback binding)
                object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        mVerificationInProgress = false
                        // signInWithPhoneAuthCredential(credential)


                    }

                    override fun onVerificationFailed(exception: FirebaseException?) {

                        mVerificationInProgress = false
                        if (exception is FirebaseAuthInvalidCredentialsException) {

                        } else if (exception is FirebaseTooManyRequestsException) {

                        }

                    }
                }, // OnVerificationStateChangedCallbacks
                token)             // ForceResendingToken from callbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        splashNavigationListener.loadNotification()

                    }
                }
    }

    private fun signOut() {
        mAuth!!.signOut()
       // updateUI(STATE_INITIALIZED)
    }



}
