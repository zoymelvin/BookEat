package com.jif.bookeat

import android.os.Parcel
import android.os.Parcelable

data class pesananMenuItem(
    val namaMenu: String,
    val fotoMenu: String,
    val harga: Int,
    var isSelected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(namaMenu)
        parcel.writeString(fotoMenu)
        parcel.writeInt(harga)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<pesananMenuItem> {
        override fun createFromParcel(parcel: Parcel): pesananMenuItem {
            return pesananMenuItem(parcel)
        }

        override fun newArray(size: Int): Array<pesananMenuItem?> {
            return arrayOfNulls(size)
        }
    }
}
