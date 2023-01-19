package com.example.esmnapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.example.esmnapplication.R.*

class MainActivity : AppCompatActivity() {
    val contenidoIntentImplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        if (result.resultCode == RESULT_OK){
            if (result.data!!.data != null) {
                val uri: Uri = result.data!!.data!!
                val cursor = contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null,
                    null
                )
                cursor?.moveToFirst()
                val indiceTelefono = cursor?.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                )
                val telefono = cursor?.getString(
                    indiceTelefono!!
                )
                cursor?.close()
                Log.i("intent-epn", "Telefono ${telefono}")
            }
        }
    }

    val contenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        if (result.resultCode == RESULT_OK){
            if (result.data != null) {
                val data = result.data
                Log.i("intent-epn","${data?.getStringExtra("nombreModificado")}")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main) //apunta a la carpeta de recursos

        EBaseDeDatos.tablaEntrenador = ESqliteHelperEntrenador(this)


        val botonCicloVida = findViewById<Button>(id.btn_ciclo_vida)
        botonCicloVida.setOnClickListener{
            irActividad(ACicloVida::class.java)
        }
        val botonList = findViewById<Button>(id.btn_ir_list_view)
        botonList.setOnClickListener{irActividad(BListView::class.java)}

        val botonIntentImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonIntentImplicito.setOnClickListener{
            val intentConRespuesta = Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            contenidoIntentImplicito.launch(intentConRespuesta)
        }
        val botonIntentExplicito = findViewById<Button>(R.id.btn_intent)
        botonIntentExplicito.setOnClickListener{ abrirActividadConParametros(CIntentExplicitoParametros::class.java)

        }
        val btnSqlite = findViewById<Button>(R.id.btn_sqlite)
        btnSqlite.setOnClickListener{
            irActividad(ECrudEntrenador::class.java)
        }
    }

    fun abrirActividadConParametros(
        clase: Class<*>
    ){
        val intentExplicito = Intent(this,clase)
        // enviar parametros (primitivas)
        intentExplicito.putExtra("nombre","Erick")
        intentExplicito.putExtra("apellido","Muñoz")
        intentExplicito.putExtra("edad",22)
        intentExplicito.putExtra("entrenadorPrincipal",
            BEntrenador(1,"Erick","paleta"))
        contenidoIntentExplicito.launch(intentExplicito)

    }


    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

}