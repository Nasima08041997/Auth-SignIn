package com.auth.android.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import auth.authPres
import com.auth.android.R
import com.auth.android.R.id.*
import com.auth.android.utils.kotlin.hideProgress
import com.auth.android.utils.kotlin.showProgress
import com.fitlinks.authentication.AuthView
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseUser

import com.auth.android.utils.kotlin.hide
import com.auth.android.utils.kotlin.hideProgress
import com.auth.android.utils.kotlin.show

import kotlinx.android.synthetic.main.fragment_signup_signin.*
import com.auth.android.utils.kotlin.showProgress
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.internal.se
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.fragment_signup_signin.view.*
import kotlinx.android.synthetic.main.fragment_welcome.*


/**
 * Created by nasima on 28/09/17.
 */
 class SignUpSignInFragment() : Fragment(),View.OnClickListener, AuthView, CompoundButton.OnCheckedChangeListener {


    lateinit var mAuthType: String
    lateinit var authPresenter:AuthPresenter
    var mAuth = getFirebaseAuth()
    var currentUser: FirebaseUser? = null

    lateinit var callbackManager: CallbackManager
    lateinit var mTwitterAuthClient: TwitterAuthClient



    fun getFirebaseAuth(): FirebaseAuth {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_signup_signin, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fbButton.setOnClickListener(this)
        gmailButton.setOnClickListener(this)
        twitterButton.setOnClickListener(this)
        signupButton.setOnClickListener(this)
        loginButton.setOnClickListener(this)
        signupRadioButton.setOnCheckedChangeListener(this)
        loginRadioButton.setOnCheckedChangeListener(this)
        authPresenter = AuthPresenter(activity, this)
        data(authPresenter)
        authenticateUser()


        callbackManager = CallbackManager.Factory.create()
       // mTwitterAuthClient = TwitterAuthClient()



        signupRadioButton.isChecked = true
        toggleViews("signup")

    }

    fun authenticateUser() {
        currentUser= mAuth.currentUser
    }

    lateinit var splashNavigationListener: SplashNavigationListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashNavigationListener) {
            splashNavigationListener = context
        }

    }


    override fun onClick(view: View?) {

        when(view!!.id)
        {
            R.id.gmailButton -> {
                gmailAction()
            }
            R.id.signupButton -> {
                signUp()
            }
            R.id.loginButton -> {
                logIn()
            }
            R.id.fbButton -> {

               // fbAction()

            }
            R.id.twitterButton -> {
               // twitterAction()

            }
        }

    }



    fun signUp()
    {
        if(validate()) {
            if (isValidEmail(EditUserEmail.getText().toString())) {
                val data = DataModel(userName= EditUserName.getText().toString(),userEmail = EditUserEmail.getText().toString(),userPassword = EditUserPassword.getText().toString())
                signUpUserdata().push().setValue(data)
                authPresenter.signUpWithEmail(EditUserEmail.text.toString(),EditUserPassword.text.toString())
                splashNavigationListener.loadWelcomeFragment()
                splashNavigationListener.loginData(data)
            }
            else
                UserEmail.error = "Enter Valid Email"
        }

    }

    fun signUpUserdata(): DatabaseReference {
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        return mDatabaseRef.child("SignUp_UserData")
    }
    fun logInUserdata(): DatabaseReference {
        val mDatabaseRef = FirebaseDatabase.getInstance().reference
        return mDatabaseRef.child("logIn_UserData")
    }

    fun logIn()
    {
        if(validate())
        {
            val data = DataModel(userEmail= EditUserEmail.getText().toString(),userPassword = EditUserPassword.getText().toString())
            logInUserdata().push().setValue(data)
            authPresenter.signInWithEmail(EditUserEmail.text.toString(),EditUserPassword.text.toString())
            splashNavigationListener.loadWelcomeFragment()
            splashNavigationListener.loginData(data)


        }
    }


    fun data(authPresenter: AuthPresenter)
    {
        splashNavigationListener.setauthPresenter(authPresenter)
    }

    private fun twitterAction() {
        mAuthType = "Twitter"
        authPresenter.authorizeWithTwitter(mTwitterAuthClient)
    }

    private fun gmailAction() {
        mAuthType = "google"
        authPresenter.signInWithGoogle()
    }

    private fun fbAction() {
        mAuthType = "facebook"
        authPresenter.authorizeWithFB(callbackManager)
    }


    override fun onCheckedChanged(button: CompoundButton?, isSelected: Boolean) {
        if(isSelected) {
            Log.i("selected","true")
            when (button!!.id) {
                R.id.signupRadioButton -> {
                    Log.i("selected","signup")

                    toggleViews("signup")
                }
                R.id.loginRadioButton -> {
                    Log.i("selected","login")

                    toggleViews("login")
                }
            }
        }
    }

    lateinit var mMode: String

    private fun toggleViews(mode: String) {

        this.mMode = mode

        when (mMode) {
            "signup" -> {
                signupButton.show()
                confirmPassword.show()
                UserName.show()
                UserEmail.show()
                confirmPassword.show()
                loginButton.hide()
            }
            "login" -> {

                loginButton.show()
                UserName.hide()
                confirmPassword.show()
                UserEmail.show()
                confirmPassword.hide()
                signupButton.hide()
            }
        }

    }

    private fun validate(): Boolean {
        var isValid: Boolean = false
        var isEmpty: Boolean
        var isMatch: Boolean

        UserName.isErrorEnabled = false
        UserPassword.isErrorEnabled =false
        confirmPassword.isErrorEnabled = false
        UserEmail.isErrorEnabled=false
        when (mMode) {
            "signup" -> {
                var isEmpty = EditUserName.text.isNullOrEmpty()
                UserName.isErrorEnabled = isEmpty
                if (isEmpty) {
                    UserName.error = "Required"
                }

                isEmpty = EditUserEmail.text.isNullOrEmpty()
                UserEmail.isErrorEnabled = isEmpty
                if (isEmpty) {
                    UserEmail.error = "Required"
                }

                isEmpty = EditUserPassword.text.isNullOrEmpty()
                UserPassword.isErrorEnabled = isEmpty
                if (isEmpty) {
                    UserPassword.error = "Required"
                }

                isEmpty = EditconfirmPassword.text.isNullOrEmpty()
                confirmPassword.isErrorEnabled = isEmpty
                if (isEmpty) {
                    confirmPassword.error = "Required"
                }


                isMatch =EditUserPassword.getText().toString().equals(EditconfirmPassword.getText().toString())
                if(isMatch)
                {
                    isMatch =  true
                }
                else
                {
                    confirmPassword.isErrorEnabled = isMatch
                    confirmPassword.error = "Confirm password should match with Password"
                }

                if (!isEmpty && isMatch) {
                    isValid = !isEmpty
                }


            }
            "login" -> {
                isEmpty = EditUserName.text.isNullOrEmpty()
                UserName.isErrorEnabled = isEmpty
                if (isEmpty) {
                    UserName.error = "Required"
                }

                isEmpty = EditUserPassword.text.isNullOrEmpty()
                UserPassword.isErrorEnabled = isEmpty
                if (isEmpty) {
                    UserPassword.error = "Required"
                }
                if (!isEmpty)
                    isValid = !isEmpty
            }
        }

        return isValid
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        if (target == null) {
            return false
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                Toast.makeText(context, "Logged in Successfully..", Toast.LENGTH_LONG).show()
                val account = result.signInAccount
                if (account != null) {
                    loadwelcome()

                    authPresenter.firebaseAuthWithGoogle(account)
                }
            } else {
                onError("Unable to Sign In")
            }
        } else {

        }
    }



    override fun updateUI(user: FirebaseUser?) {
        Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
        //if (AuthenticationApplication.getPreferences().getUser() == null) {
          //  val userObject = User(
                   // user?.displayName!!,
                           // user.email!!
            //)
           // AuthenticationApplication.getPreferences()!!.setUser(userObject)
       // }
        loadwelcome()
    }

    override fun hideProgress() {
            activity.hideProgress()
    }

    override fun onError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun showProgress(message: String) {
        activity.showProgress(message)
    }

    fun loadwelcome(){
        splashNavigationListener.loadWelcomeFragment()

    }
}
