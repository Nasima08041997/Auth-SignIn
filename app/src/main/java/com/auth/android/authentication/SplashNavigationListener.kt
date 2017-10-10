package com.auth.android.authentication

/**
 * Created by nasima on 28/09/17.
 */
interface SplashNavigationListener {
    fun loadSignupFragment()
    fun loadWelcomeFragment()
    fun phonenumberFragment()
    fun loadOtpFragment()
    fun loademailFragment()
    fun setauthPresenter(authPresenter: AuthPresenter)
    fun getauthPresenter():AuthPresenter
    fun loginData(dataModel: DataModel)
    fun getData():DataModel
    fun getPhoneNumber() : String
    fun setPhoneNumber(phonenumber : String)


    fun loadNotification()


    fun getOTP(): String
    fun setOTP(OTP: String)

}