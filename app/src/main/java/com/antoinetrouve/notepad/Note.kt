package com.antoinetrouve.notepad

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Manage Note
 * implement Parcelable (extra) and Serializable (file)
 */
data class Note(var title: String = "",
                var text: String = "",
                var filename: String = "") : Parcelable, Serializable {

    /**
     * Construct to share Note
     * @var Parcel parcel The parcel element
     */
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    /**
     * Serialize Note for Extra
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(filename)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        // unique id
        private val serialVersionUid: Long = 1515151515

        /** Deserialize Parcel to create a Note */
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}