package com.auth.android.authentication

/**
 * Created by nasima on 28/09/17.
 */
interface SplashNavigationListener {
    fun loadSignupFragment()
    fun loadWelcomeFragment()
    fun setauthPresenter(authPresenter: AuthPresenter)
    fun getauthPresenter():AuthPresenter

}