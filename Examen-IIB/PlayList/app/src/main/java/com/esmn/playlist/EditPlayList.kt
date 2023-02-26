package com.esmn.playlist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditPlayList : AppCompatActivity() {
    val firestoreDAO = FireStoreDAO()
    lateinit var idPlayList: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_play_list)
        // Referencia a los elementos del layout
        val nombreInput = findViewById<EditText>(R.id.id_nombre_input_playlist)
        val duracionInput = findViewById<EditText>(R.id.id_duracion_input_playlist)
        val crearButton = findViewById<Button>(R.id.id_create_edit_playlist)
        // Obtener la id de la playlist y la cancion

        val playlist = intent.getParcelableExtra<PlayList>("playlist")

            if (playlist != null){
                idPlayList = playlist.id!!
                nombreInput.setText(playlist.nombre)
                duracionInput.setText(playlist.descripcion)
                crearButton.setOnClickListener {
                // Obtener los valores ingresados
                val nombre = nombreInput.text.toString()
                val duracion = duracionInput.text.toString()
                val newPlaylist = PlayList(playlist.id ,nombre, duracion)
                firestoreDAO.actualizarPlaylist(idPlayList,newPlaylist,{
                    Toast.makeText(this, "Se actualizo la Playlist ${playlist.nombre} correctamente", Toast.LENGTH_SHORT)
                        .show()
                    setResult(Activity.RESULT_OK, Intent().putExtra("playlistActualizada", newPlaylist))
                    finish()
                }, { exception ->
                    Toast.makeText(this, "Error al actualizar la Playlist: $exception", Toast.LENGTH_SHORT)
                        .show()
                })

            }
        }else{
            crearButton.setOnClickListener {
                // Obtener los valores ingresados
                val nombre = nombreInput.text.toString()
                val duracion = duracionInput.text.toString()

                // Crear la nueva canción
                val playList = PlayList(nombre,duracion, emptyList())
                firestoreDAO.crearPlaylist(playList,
                    {
                        playList.id = it
                        Toast.makeText(this, "Se creo la Playlist ${playList.nombre} correctamente", Toast.LENGTH_SHORT)
                            .show()
                        setResult(Activity.RESULT_OK, Intent().putExtra("playlistActualizada", playList))
                        finish()
                    }
                    , { exception ->
                        // Manejar el error si ocurre
                        Toast.makeText(this, "Error al actualizar la Playlist: $exception", Toast.LENGTH_SHORT)
                            .show()
                    })
            }
        }
    }


}