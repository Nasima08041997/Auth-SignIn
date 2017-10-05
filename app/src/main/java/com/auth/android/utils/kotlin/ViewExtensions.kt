package com.auth.android.utils.kotlin

import android.app.Activity
import android.app.ProgressDialog
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by nasima on 28/09/17.
 */

    fun View.isVisible() = visibility == View.VISIBLE

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun View.getString(res: Int) = resources.getString(res)

    fun View.hideKeyboard(inputMethodManager: InputMethodManager) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.showKeyboard(inputMethodManager: InputMethodManager) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    var progressDialog: ProgressDialog? = null

    fun Activity.showProgress(message: String): ProgressDialog {
        progressDialog = ProgressDialog(window.context)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage(message)
        progressDialog!!.show()
        return progressDialog as ProgressDialog
    }

    fun Activity.hideProgress() {
        progressDialog!!.cancel()
    }
