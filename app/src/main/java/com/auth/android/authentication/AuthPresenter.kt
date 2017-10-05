package com.auth.android.authentication

import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.auth.android.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fitlinks.authentication.AuthView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User

/**
 * Created by nasima on 28/09/17.
 */
class AuthPresenter(val activity: FragmentActivity, val authView: AuthView) : GoogleApiClient.OnConnectionFailedListener {


    private var mAuth: FirebaseAuth? = null
    private var RC_SIGN_IN = 111
    private var SIGN_OUT=112
    private var mGoogleApiClient: GoogleApiClient?
    var fbPermissions: ArrayList<String> = ArrayList()



    init {
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        fbPermissions = arrayListOf("email", "public_profile", "user_birthday")

        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    fun signInWithGoogle() {

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        authView.showProgress(activity.getString(R.string.signing_in))
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        signInWithCredential(credential)
    }

    lateinit var mTwitterAuthClient: TwitterAuthClient

    fun authorizeWithTwitter(twitterAuthClient: TwitterAuthClient) {
        mTwitterAuthClient = twitterAuthClient
        authView.showProgress(activity.getString(R.string.signing_in))
        twitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                handleTwitterSession(result!!.data)
            }

            override fun failure(exception: TwitterException?) {
                exception!!.printStackTrace()
                authView.hideProgress()
                authView.onError(exception.message!!)
            }
        })
    }

    private fun handleTwitterSession(data: TwitterSession?) {
        mTwitterAuthClient.requestEmail(data, object : Callback<String>() {
            override fun success(result: Result<String>) {
                connectUserToFirebase(data)
                // Do something with the result, which provides the email address
            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
                exception.printStackTrace()
                authView.hideProgress()
                authView.onError(exception.message!!)
            }
        })
    }


    private fun connectUserToFirebase(session: TwitterSession?) {
        val credential = TwitterAuthProvider.getCredential(
                session!!.authToken.token,
                session!!.authToken.secret)
        val twitterName = session.userName
        signInWithCredential(credential)

    }

    private fun signInWithCredential(credential: AuthCredential?) {
        mAuth!!.signInWithCredential(credential!!)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.getCurrentUser()
                        fetchUserDetails(user)
                    } else
                        authView.hideProgress()
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    authView.hideProgress()
                    if (exception.message.equals(activity.getString(R.string.link_msg))) {
                        authView.onError(activity.getString(R.string.link_msg))
                    }
                }

    }

    private fun fetchUserDetails(user: FirebaseUser?) {

       getFirebaseDatabase()
                .child("users")
                .child(user!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {

                        if (error != null)
                            authView.onError(error.message)

                        authView.hideProgress()
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        if (dataSnapshot != null && dataSnapshot.value != null) {
                            Log.i("if---->" ,"true")
                            val flUser = dataSnapshot.getValue(User::class.java) as User
                            AuthenticationApplication.getPreferences().setUser(flUser)
                            authView.updateUI(user)
                        } else {
                            Log.i("else---->" ,"true")
                            authView.updateUI(user)
                        }
                        authView.hideProgress()

                    }

                })
    }

    fun getFirebaseDatabase(): DatabaseReference {
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        return mDatabaseRef
    }

    fun authorizeWithFB(callbackManager: CallbackManager) {
        authView.showProgress(activity.getString(R.string.signing_in))
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(activity, fbPermissions)
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                        signInWithCredential(credential)
                    }

                    override fun onCancel() {
                        Toast.makeText(activity, "Facebook authentication cancelled", Toast.LENGTH_SHORT).show()
                    }


                    override fun onError(exception: FacebookException) {
                        authView.onError(exception.message!!)
                    }
                })
    }

    fun signInWithEmail(email: String, password: String) {
        authView.showProgress(activity.getString(R.string.signing_in))
        mAuth!!.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(activity, { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.currentUser
                        authView.hideProgress()
                        Log.i("SIGNINEMAIL","........................task is successful"+mAuth!!.currentUser)
                        authView.updateUI(user)
                    } else {
                        authView.onError(task.exception!!.message!!)
                        authView.hideProgress()
                    }
                })
    }

    fun signUpWithEmail(email:String, password: String) {
        authView.showProgress(activity.getString(R.string.signing_in))
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, { task ->
                    if (task.isSuccessful) {
                        val user = mAuth!!.getCurrentUser()
                        authView.hideProgress()
                        authView.updateUI(user)
                    } else {
                        authView.onError(task.exception!!.message!!)
                        authView.hideProgress()
                    }

                })
    }

    fun signOut() {
        mAuth!!.signOut()
        mGoogleApiClient!!.stopAutoManage(activity)
        mGoogleApiClient!!.disconnect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}