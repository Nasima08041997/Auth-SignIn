package com.auth.android.authentication

import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract
import java.net.PasswordAuthentication

/**
 * Created by nasima on 04/10/17.
 */
data class DataModel(
    var userName: String ="",
    var userEmail: String ="",
    var userPassword: String= ""
){

}