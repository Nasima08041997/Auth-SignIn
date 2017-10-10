package com.auth.android.authentication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.auth.android.R
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_phonenumber_signup.*

/**
 * Created by nasima on 05/10/17.
 */
class PhonenumberFragment: Fragment() ,View.OnClickListener{

    lateinit var splashNavigationListener: SplashNavigationListener
    lateinit var message:String


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_phonenumber_signup, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupButtonOTP.setOnClickListener(this)


    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashNavigationListener) {
            splashNavigationListener = context
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.signupButtonOTP -> {
                splashNavigationListener.setPhoneNumber(EditTextPhoneNumber.text.toString())
               splashNavigationListener.loadOtpFragment()
            }
        }

    }





}