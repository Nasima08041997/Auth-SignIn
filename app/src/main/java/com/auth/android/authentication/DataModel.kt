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
    var userPassword: String =""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(userEmail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }

}