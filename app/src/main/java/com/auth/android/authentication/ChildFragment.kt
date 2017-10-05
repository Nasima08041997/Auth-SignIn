package com.auth.android.authentication


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.auth.android.R
import kotlinx.android.synthetic.main.fragment_child.*

/**
 * Created by nasima on 05/10/17.
 */
class ChildFragment : Fragment() {

    lateinit var splash : SplashNavigationListener
    lateinit var dataModel : DataModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashNavigationListener) {
            splash = context
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_child, container, false)


    }



    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataModel = splash.getData()
        emailTV.text = dataModel.userEmail

    }
}