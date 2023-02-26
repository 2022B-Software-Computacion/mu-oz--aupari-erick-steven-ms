package com.esmn.playlist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditSong : AppCompatActivity() {
    val firestoreDAO = FireStoreDAO()
    lateinit var idPlayList: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_song)

        // Referencia a los elementos del layout
        val nombreInput = findViewById<EditText>(R.id.id_nombre_input)
        val duracionInput = findViewById<EditText>(R.id.id_duracion_input)
        val crearButton = findViewById<Button>(R.id.id_create_edit_song)
        // Obtener la id de la playlist y la cancion
        idPlayList = intent.getStringExtra("idPlaylist")!!
        val cancion = intent.getParcelableExtra<Cancion>("cancion")
        if (cancion != null){
            nombreInput.setText(cancion.nombre)
            duracionInput.setText(cancion.duracion)
            crearButton.setOnClickListener {
                // Obtener los valores ingresados
                val nombre = nombreInput.text.toString()
                val duracion = duracionInput.text.toString()
                val newCancion = Cancion(cancion.id ,nombre, duracion)
                firestoreDAO.actualizarCancion(idPlayList,newCancion,{
                    Toast.makeText(this, "Se actualizo la cancion ${cancion.nombre} correctamente", Toast.LENGTH_SHORT)
                        .show()
                    setResult(Activity.RESULT_OK, Intent().putExtra("cancionActualizada", newCancion))
                    finish()
                }, { exception ->
                    Toast.makeText(this, "Error al actualizar la cancion: $exception", Toast.LENGTH_SHORT)
                        .show()
                })

            }
        }else{
            crearButton.setOnClickListener {
                // Obtener los valores ingresados
                val nombre = nombreInput.text.toString()
                val duracion = duracionInput.text.toString()


                // Crear la nueva canción
                val cancion = Cancion(nombre,duracion)
                firestoreDAO.agregarCancion(idPlayList ,cancion,
                    {
                        Toast.makeText(this, "Se agrego la cancion ${cancion.nombre} correctamente", Toast.LENGTH_SHORT)
                            .show()
                        setResult(Activity.RESULT_OK, Intent().putExtra("cancionActualizada", cancion))
                        finish()
                    }
                    , { exception ->
                        // Manejar el error si ocurre
                        Toast.makeText(this, "Error al agregar la playlist: $exception", Toast.LENGTH_SHORT)
                            .show()
                    })
            }
        }

    }
}
