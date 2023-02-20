package com.esmn.telegramclone

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class Chat(
    val name: String?,
    val listMessages: ArrayList<Message>,
    val thumbnail: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readArrayList(Message::class.java.classLoader) as ArrayList<Message>,
        parcel.readInt()
    )


    val lastMessage: String = listMessages[listMessages.size - 1].text
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeList(listMessages)
        parcel.writeInt(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}