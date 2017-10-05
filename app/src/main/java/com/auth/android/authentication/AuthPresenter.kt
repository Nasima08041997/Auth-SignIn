package com.auth.android.authentication

import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.auth.android.R
import com.fitlinks.authentication.AuthView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Created by nasima on 28/09/17.
 */
class AuthPresenter(val activity: FragmentActivity, val authView: AuthView) : GoogleApiClient.OnConnectionFailedListener {


    private var auth: FirebaseAuth? = null
    private var RC_SIGN_IN = 111
    private var SIGN_OUT=112
    private var mGoogleApiClient: GoogleApiClient?



    init {
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    fun signInWithGoogle() {

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    fun signOutWithGoogle()
    {
        mGoogleApiClient!!.stopAutoManage(activity);
        mGoogleApiClient!!.disconnect()
    }



    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}