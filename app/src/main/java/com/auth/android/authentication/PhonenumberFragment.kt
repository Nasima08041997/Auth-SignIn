package com.auth.android.authentication

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.auth.android.R

/**
 * Created by nasima on 05/10/17.
 */
class PhonenumberFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_phonenumber_signup, container, false)
        return view
    }



}