package com.esmn.playlist

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreDAO {

    private val db = FirebaseFirestore.getInstance()
    private val playlistsRef = db.collection("playlists")

    // Crear una nueva playlist
    fun crearPlaylist(
        playlist: PlayList,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Agregar la playlist a Firestore
        playlistsRef.add(playlist)
            .addOnSuccessListener { documentReference ->
                // Obtener referencia a la subcolección "canciones" dentro de la playlist creada
                val cancionesRef = documentReference.collection("canciones")

                // Agregar cada canción a la subcolección "canciones" dentro de la playlist
                for (cancion in playlist.canciones) {
                    cancionesRef.add(cancion)
                        .addOnSuccessListener {
                            cancion.id = it.id
                        }
                        .addOnFailureListener { onFailure(it) }
                }

                onSuccess(documentReference.id)
            }
            .addOnFailureListener { onFailure(it) }
    }

    // Actualizar una playlist existente
    fun actualizarPlaylist(
        idPlaylist: String,
        playlist: PlayList,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        playlistsRef.document(idPlaylist).set(playlist)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Eliminar una playlist existente
    fun eliminarPlaylist(
        idPlaylist: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        playlistsRef.document(idPlaylist).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Obtener todas las playlists
    fun getPlaylists(onSuccess: (List<PlayList>) -> Unit, onFailure: (Exception) -> Unit) {
        playlistsRef.get()
            .addOnSuccessListener { result ->
                val playlists = mutableListOf<PlayList>()
                for (document in result) {
                    val playlist = document.toObject(PlayList::class.java)
                    playlist.id = document.id
                    playlists.add(playlist)
                    getCancionesDePlaylist(playlist.id!!, onSuccess = {
                        playlist.canciones = it
                    }, onFailure = {})
                }
                onSuccess(playlists)
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error al obtener las playlists", exception)
                onFailure(exception)
            }
    }


    // Obtener todas las canciones de una playlist
    fun getCancionesDePlaylist(
        idPlaylist: String,
        onSuccess: (List<Cancion>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val cancionesRef = playlistsRef.document(idPlaylist).collection("canciones")

        cancionesRef.get()
            .addOnSuccessListener { result ->
                val canciones = mutableListOf<Cancion>()
                for (document in result) {
                    val cancion = document.toObject(Cancion::class.java)
                    cancion.id = document.id
                    canciones.add(cancion)
                }
                onSuccess(canciones)
            }
            .addOnFailureListener { onFailure(it) }
    }

    /* Agregar una nueva canción a una playlist existente
    fun agregarCancion(
        idPlaylist: String,
        cancion: Cancion,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Obtener referencia a la playlist
        val playlistRef = playlistsRef.document(idPlaylist)
        playlistRef.get()
            .addOnSuccessListener { playlistDoc ->
                // Obtener matriz de canciones y asignar un nuevo ID a la nueva canción
                val canciones = playlistDoc.get("canciones") as? List<Cancion> ?: emptyList()


                // Agregar la nueva canción a la matriz de canciones
                val nuevasCanciones = canciones + cancion

                // Actualizar el campo canciones en Firestore con la nueva matriz
                playlistRef.update("canciones", nuevasCanciones)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }*/

    // Actualizar una canción existente dentro de una playlist
    fun actualizarCancion(idPlaylist: String, cancion: Cancion, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cancionesRef = playlistsRef.document(idPlaylist).collection("canciones")
        cancionesRef.document(cancion.id!!).set(cancion)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Actualizar una canción existente dentro de una playlist
    fun agregarCancion(idPlaylist: String, cancion: Cancion, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val cancionesRef = playlistsRef.document(idPlaylist).collection("canciones")
        cancionesRef.add(cancion)
            .addOnSuccessListener {
                cancion.id = it.id
                onSuccess()
            }
            .addOnFailureListener { onFailure(it) }
    }

    // Eliminar una canción existente dentro de una playlist
    fun eliminarCancion(
        idPlaylist: String,
        idCancion: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Obtener referencia al documento de la canción dentro de la subcolección
        val cancionRef =
            playlistsRef.document(idPlaylist).collection("canciones").document(idCancion)

        cancionRef.get()
            .addOnSuccessListener { cancionDoc ->
                // Verificar que el documento exista antes de eliminarlo
                if (cancionDoc.exists()) {
                    // Eliminar el documento de la subcolección
                    cancionRef.delete()
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it) }
                } else {
                    onFailure(Exception("No se encontró la canción con el ID $idCancion"))
                }
            }
            .addOnFailureListener { onFailure(it) }
    }



}
