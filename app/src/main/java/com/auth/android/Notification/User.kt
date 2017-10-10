package com.fitlinks.onboarding

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Alok on 11/09/17.
 */
data class User(var userId: String = "",
                var userName: String = "",
                var email: String = "",
                var mobile: String = "",
                var photoUrl: String = "",
                var authType: String = "",
               // var profileCompletion: Int = NONE,
                var dob: String = "",
                var gender: String = "",
                var address1: String = "",
                var address2: String = "",
                var city: String = "",
                var zipCode: String = "",
                var country: String = "",
                var occupation: String = "",
                var bio: String = "",
                var userType: String = "Buddy"
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            //source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userId)
        writeString(userName)
        writeString(email)
        writeString(mobile)
        writeString(photoUrl)
        writeString(authType)
        //writeInt(profileCompletion)
        writeString(dob)
        writeString(gender)
        writeString(address1)
        writeString(address2)
        writeString(city)
        writeString(zipCode)
        writeString(country)
        writeString(occupation)
        writeString(bio)
        writeString(userType)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}