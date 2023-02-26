package com.esmn.playlist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts


class ListSongsActivity : AppCompatActivity() {
    lateinit var listViewCanciones: ListView
    lateinit var nombrePlayListTextView: TextView

    val firestoreDAO = FireStoreDAO()

    var idItemSeleccionado = 0
    var canciones: List<Cancion>? = null
    lateinit var nombreCanciones: ArrayList<String>
    lateinit var nombrePlaylist: String
    lateinit var idPlayList: String
    private val updateSongLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { cancionActualizada ->
        if (cancionActualizada.resultCode == Activity.RESULT_OK) {
            val updatedCancion = cancionActualizada.data?.getParcelableExtra<Cancion>("cancionActualizada")
            Log.e("TAG", updatedCancion.toString())
            // Actualizar la canción en el listview
            if (updatedCancion != null) {
                // Buscar y actualizar la canción en la lista de la playlist
                val index: Int = canciones!!.indexOfFirst { it.id == updatedCancion.id }
                if (index >= 0) {
                    val updatedCanciones = canciones!!.toMutableList()
                    updatedCanciones[index] = updatedCancion
                    canciones = updatedCanciones.toList()
                    // Aquí se actualiza con la nueva canción la lista local
                    nombreCanciones[index] = updatedCancion.nombre
                    Log.e("TAG", nombreCanciones.toString())
                    // Actualizar la vista de la lista
                    (listViewCanciones.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                }else{
                    val updatedCanciones = canciones!!.toMutableList()
                    updatedCanciones.add(updatedCancion)
                    canciones = updatedCanciones.toList()
                    // Aquí se actualiza con la nueva canción la lista local
                    nombreCanciones.add(updatedCancion.nombre)
                    Log.e("TAG", nombreCanciones.toString())
                    // Actualizar la vista de la lista
                    (listViewCanciones.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                }
            }

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_songs)
        val btnCreateSong = findViewById<Button>(R.id.id_btn_createSong)
        btnCreateSong.setOnClickListener{
            editarCreateCancion(null)
        }
        // Obtener el ListView de las canciones y textview
        nombrePlayListTextView = findViewById<TextView>(R.id.id_textview_nombrePlaylist)
        listViewCanciones = findViewById<ListView>(R.id.id_listview_canciones)

        // Obtener la lista de canciones de la playlist seleccionada, su nombre e id
        idPlayList = intent.getStringExtra("idPlaylist")!!
        nombrePlaylist = intent.getStringExtra("playlistName")!!
        canciones = intent.getParcelableArrayListExtra<Cancion>("canciones")

        nombreCanciones = canciones?.map { it.nombre } as ArrayList<String>

        // Crear un adaptador para la lista de canciones
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, nombreCanciones as List<String>)

        // Asignar el adaptador al ListView
        listViewCanciones.adapter = adapter

        // Actualizar la vista del ListView y asignar al textview el nombre de la playlist
        nombrePlayListTextView.text = nombrePlaylist
        adapter.notifyDataSetChanged()

        registerForContextMenu(listViewCanciones)
    }



    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu_songs, menu)
        // obtener el id del array
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        idItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.song_editar -> {
                editarCreateCancion(canciones!![idItemSeleccionado])
                return true
            }
            R.id.song_eliminar -> {
                eliminarCancion()
                return true
            }


            else -> super.onContextItemSelected(item)
        }
    }

    fun editarCreateCancion(
        cancion: Cancion?
    ){
        // Crear un intent con el id de la playlist
        val intent = Intent(this, EditSong::class.java)
        intent.putExtra("idPlaylist", idPlayList)
        intent.putExtra("cancion", cancion)

        // Iniciar la actividad y pasar los datos
        updateSongLauncher.launch(intent)
    }

    fun eliminarCancion() {
        //se toma el indice de la lista para identificar la cancion
        val cancionIndex = idItemSeleccionado
        val cancionID = canciones!![idItemSeleccionado].id!!
        // se elimina el elemento del documento de la playlist definida
        firestoreDAO.eliminarCancion(
            idPlayList, cancionID,
            onSuccess = {
                Toast.makeText(this, "Cancion eliminada con exito", Toast.LENGTH_SHORT)
                    .show()
                canciones = canciones?.filterIndexed { index, _ -> index != cancionIndex }
                nombreCanciones.removeAt(cancionIndex)
                (listViewCanciones.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            },
            onFailure = { error ->
                Toast.makeText(
                    this,
                    "Error al eliminar la cancion: $error",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Error al eliminar", error.toString())
            }
        )
    }


}

