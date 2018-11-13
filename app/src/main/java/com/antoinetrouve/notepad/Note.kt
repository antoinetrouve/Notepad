package com.antoinetrouve.notepad

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Note(var title: String = "",
                var text: String = "",
                var filename: String = "") : Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(filename)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        // unique id to retrieve class note version by JVM
        private val serialVersionUid: Long = 1515151515
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}