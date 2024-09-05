package com.example.scheduledappcontroller

import android.os.Parcel
import android.os.Parcelable

data class Mode(
    val name: String,
    val startTime: String, // Added start time for the mode
    val endTime: String,   // Added end time for the mode
    val blockedApps: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",  // Read start time
        parcel.readString() ?: "",  // Read end time
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(startTime) // Write start time
        parcel.writeString(endTime)   // Write end time
        parcel.writeStringList(blockedApps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mode> {
        override fun createFromParcel(parcel: Parcel): Mode {
            return Mode(parcel)
        }

        override fun newArray(size: Int): Array<Mode?> {
            return arrayOfNulls(size)
        }
    }
}
