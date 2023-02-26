package com.esmn.playlist

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlin.reflect.KClass

data class Cancion(
    @Exclude @JvmField var id: String?,
    val nombre: String = "",
    val duracion: String = ""
): Parcelable {
    // constructor vacio usado por firebase
    constructor() : this(null, "", "")
    // constructor de objetos
    constructor(nombre: String,duracion: String) : this(null, nombre, duracion)

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(duracion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cancion> {
        override fun createFromParcel(parcel: Parcel): Cancion {
            return Cancion(parcel)
        }

        override fun newArray(size: Int): Array<Cancion?> {
            return arrayOfNulls(size)
        }
    }




}
