package com.auth.android.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import auth.authPres
import com.auth.android.R
import com.auth.android.utils.kotlin.hideProgress
import com.auth.android.utils.kotlin.showProgress
import com.fitlinks.authentication.AuthView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_child.*
import kotlinx.android.synthetic.main.fragment_signup_signin.*
import kotlinx.android.synthetic.main.fragment_welcome.*

/**
 * Created by nasima on 29/09/17.
 */
class WelcomeFragment() :  Fragment() ,View.OnClickListener ,AuthView  {


    lateinit var mAuthPresenter:AuthPresenter
    lateinit var splashNavigationListener: SplashNavigationListener



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_welcome, container, false)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashNavigationListener) {
            splashNavigationListener = context
        }
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuthPresenter= splashNavigationListener.getauthPresenter()
        signOutButton.setOnClickListener(this)
        childButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0?.id == R.id.signOutButton)
        {
            signOut()
        }
        if(p0?.id == R.id.childButton)
        {
            loadChildFragment()
        }
    }

    fun signOut() {
        mAuthPresenter.signOut()
        Toast.makeText(context,"Successfully logged out",Toast.LENGTH_LONG).show()
        splashNavigationListener.loadSignupFragment()
    }

    fun loadChildFragment()
    {
        Toast.makeText(context,"child fragment",Toast.LENGTH_LONG).show()
        val childFragment = ChildFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.child_fragment_container, childFragment).commit()
    }


    private  fun gmailSignOutAction()
    {

        splashNavigationListener.loadSignupFragment()
    }

    override fun updateUI(user: FirebaseUser?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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



}