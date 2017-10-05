package com.auth.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.auth.android.authentication.*

class SplashActivity : AppCompatActivity() , SplashNavigationListener  {


    lateinit var authPresenter : AuthPresenter
    lateinit var dataModel : DataModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        phonenumberFragment()
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

    lateinit var PhonenumberFragment: PhonenumberFragment
    override fun phonenumberFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
       PhonenumberFragment =PhonenumberFragment()
        fragmentTransaction.replace(R.id.container, PhonenumberFragment).commit()
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


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

}
