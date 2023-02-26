package com.esmn.playlist

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class PlayList(
    @Exclude @JvmField  var id: String?, // Se excluye la propiedad "id" de Firestore
    val nombre: String ,
    val descripcion: String,
    @Exclude @JvmField var canciones: List<Cancion> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Cancion)!!
    ) {
    }

    //constructor de objetos playlist
    constructor(nombre: String, descripcion: String, canciones: List<Cancion>)
            : this(null, nombre, descripcion, canciones)
    // Constructor vacío para usar con Firebase
    constructor() : this(null, "", "", emptyList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeTypedList(canciones)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayList> {
        override fun createFromParcel(parcel: Parcel): PlayList {
            return PlayList(parcel)
        }

        override fun newArray(size: Int): Array<PlayList?> {
            return arrayOfNulls(size)
        }
    }
}
