package com.auth.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.auth.android.authentication.AuthPresenter
import com.auth.android.authentication.SignUpSignInFragment
import com.auth.android.authentication.SplashNavigationListener
import com.auth.android.authentication.WelcomeFragment

class SplashActivity : AppCompatActivity() , SplashNavigationListener  {




    lateinit var authPresenter : AuthPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadSignupFragment()
    }

    lateinit var signupSigninFragment: SignUpSignInFragment

    override fun loadSignupFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        signupSigninFragment = SignUpSignInFragment()
        fragmentTransaction.replace(R.id.container, signupSigninFragment).commit()    }

    lateinit var welcomeFragment: WelcomeFragment
    override fun loadWelcomeFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        welcomeFragment = WelcomeFragment()
        fragmentTransaction.replace(R.id.container, welcomeFragment).commit()
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
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

}
