package com.auth.android.utils

/**
 * Created by nasima on 28/09/17.
 */
interface BaseView {
    fun showProgress( message : String )
    fun hideProgress()
    fun onError(error: String)
}