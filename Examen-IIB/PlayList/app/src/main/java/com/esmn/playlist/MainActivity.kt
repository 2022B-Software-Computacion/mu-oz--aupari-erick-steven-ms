package com.esmn.playlist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts



class MainActivity : AppCompatActivity() {
    val firestoreDAO = FireStoreDAO()
    var idItemSeleccionado = 0
    lateinit var playlists: List<PlayList>
    lateinit var playlistNames: ArrayList<String>
    lateinit var playlistListView: ListView


    private val updatePlayListLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedPlaylist = result.data?.getParcelableExtra<PlayList>("playlistActualizada")
            Log.e("TAG", updatedPlaylist.toString())
            // Actualizar la canción en el listview
            if (updatedPlaylist != null) {
                // Buscar y actualizar la playlist en la coleccion
                val index: Int = playlists!!.indexOfFirst { it.id == updatedPlaylist.id }
                if (index >= 0) {
                    val updatedCanciones = playlists!!.toMutableList()
                    updatedCanciones[index] = updatedPlaylist
                    playlists = updatedCanciones.toList()
                    // Aquí se actualiza con la nueva canción la lista local
                    playlistNames[index] = updatedPlaylist.nombre
                    Log.e("TAG", playlistNames.toString())
                    // Actualizar la vista de la lista
                    (playlistListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                }else{
                    val updatedCanciones = playlists!!.toMutableList()
                    updatedCanciones.add(updatedPlaylist)
                    playlists = updatedCanciones.toList()
                    // Aquí se actualiza con la nueva playlist la lista local
                    playlistNames.add(updatedPlaylist.nombre)
                    // Actualizar la vista de la lista
                    (playlistListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.playlistListView = findViewById<ListView>(R.id.id_listview_PlayList)
        // Obtener las playlists de Firestore
        getPlayListsIniciales()
        // colocar context menu a la listview
        registerForContextMenu(playlistListView)
        val btnCrearPlaylist: Button = findViewById<Button>(R.id.id_create_playlist)
        btnCrearPlaylist.setOnClickListener {
            editarCreatePlaylist(null)
        }

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu_main, menu)
        // obtener el i del array
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        idItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                editarCreatePlaylist(playlists!![idItemSeleccionado])
                return true
            }
            R.id.mi_eliminar -> {
                eliminarPlaylist()
                return true
            }

            R.id.mi_ver_canciones -> {
                verCanciones()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun editarCreatePlaylist(
        playList: PlayList?
    ){
        // Crear un intent con el id de la playlist
        val intent = Intent(this, EditPlayList::class.java)
        intent.putExtra("playlist", playList)

        // Iniciar la actividad y pasar los datos
        updatePlayListLauncher.launch(intent)
    }



    fun anadirPlayList(
        adaptador: ArrayAdapter<PlayList>,
        arreglo: ArrayList<PlayList>,
        dao: FireStoreDAO
    ) {
        val listSongs = arrayListOf<Cancion>()
        listSongs.add(Cancion("Die for you", "3.15"))
        listSongs.add(Cancion("Feeling", "3.15"))
        val nuevaPlaylist = PlayList("PlayList1", "pa cantar llorando",listSongs)

        dao.crearPlaylist(
            nuevaPlaylist,
            { id: String ->
                // Agregar la nueva playlist a la lista local
                nuevaPlaylist.id = id
                arreglo.add(nuevaPlaylist)

                // Notificar al adaptador
                playlistNames.add(nuevaPlaylist.nombre)
                adaptador.notifyDataSetChanged()
            },
            { exception ->
                // Manejar el error si ocurre
                Toast.makeText(this, "Error al agregar la playlist: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }



    fun eliminarPlaylist(){
        //se toma el indice de la lista para identificar la playlist
        val playlistIndex = idItemSeleccionado
        // se identifica el id del documento de cada playlist
        val playlistId = playlists[playlistIndex].id
        Log.e("TAG", playlists[playlistIndex].toString())
        // se elimina el documento de la playlist definida
        firestoreDAO.eliminarPlaylist(playlistId!!,
            onSuccess = {
                Toast.makeText(this, "PlayList eliminada con exito", Toast.LENGTH_SHORT)
                    .show()
                playlists = playlists.filterIndexed { index, _ -> index != playlistIndex }
                playlistNames.removeAt(playlistIndex)
                (playlistListView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            },
            onFailure = { error ->
                Toast.makeText(
                    this,
                    "Error al eliminar la playlist: $error",
                    Toast.LENGTH_SHORT
                ).show()
            })

    }

    fun verCanciones(){

        // Agregar listener al ListView para detectar cuando un elemento es seleccionado

        val playlist = playlists[idItemSeleccionado]

        // Crear un intent con los datos de la playlist
        val intent = Intent(this, ListSongsActivity::class.java)
        intent.putExtra("idPlaylist", playlist.id)
        intent.putExtra("playlistName", playlist.nombre)
        intent.putParcelableArrayListExtra(
            "canciones",
            ArrayList(playlist.canciones)
        )


        // Iniciar la actividad y pasar los datos de la playlist
        startActivity(intent)
    }


    fun getPlayListsIniciales(){
        firestoreDAO.getPlaylists(
            onSuccess = { playlists ->

                // se guarda la playlists
                this.playlists = playlists

                // Crear una lista de strings con los nombres de las playlists
                playlistNames = playlists.map { it.nombre } as ArrayList<String>


                // Crear un ArrayAdapter para el ListView
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, playlistNames)

                // Asignar el adapter al ListView
                playlistListView.adapter = adapter
                adapter.notifyDataSetChanged()
            },
            onFailure = { error ->
                // Manejar el error
                Toast.makeText(this, "Error al obtener las playlists: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }


}