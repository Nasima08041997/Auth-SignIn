package com.fitlinks.authentication

import com.auth.android.authentication.AuthPresenter
import com.auth.android.utils.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by nasima on 28/09/17.
 */
interface AuthView : BaseView {
    fun updateUI(user: FirebaseUser?)


}

