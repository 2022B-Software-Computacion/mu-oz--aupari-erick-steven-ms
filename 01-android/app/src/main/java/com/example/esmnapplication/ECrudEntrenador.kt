package com.example.esmnapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ECrudEntrenador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_entrenador)

        val btnBuscarBDD = findViewById<Button>(R.id.btn_buscar_bdd)
        btnBuscarBDD.setOnClickListener{
            val id = findViewById<EditText>(R.id.input_id)
            val nombre = findViewById<EditText>(R.id.input_id)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val entrendor = EBaseDeDatos.tablaEntrenador!!
                .consultarEntrenadorPorId(
                    id.text.toString().toInt()
                )
            id.setText(entrendor.id.toString())
            nombre.setText(entrendor.nombre)
            descripcion.setText(entrendor.descripcion)
        }

        val btnCrear = findViewById<Button>(R.id.btn_buscar_bdd)
        btnCrear.setOnClickListener{

            val nombre = findViewById<EditText>(R.id.input_id)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val entrendor = EBaseDeDatos.tablaEntrenador!!
                .crearEntrenador(
                    nombre.text.toString(),
                    descripcion.text.toString()
                )
        }

        val btnActualizar = findViewById<Button>(R.id.btn_actualizar_bdd)
        btnActualizar.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_id)
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            EBaseDeDatos.tablaEntrenador!!.actualizararEntrenadorFormulario(
                nombre.text.toString(), descripcion.text.toString(), id.text.toString().toInt()
            )
        }

        val btnEliminar = findViewById<Button>(R.id.btn_eliminar_bdd)
        btnEliminar.setOnClickListener {
            val id = findViewById<EditText>(R.id.input_id)
            EBaseDeDatos.tablaEntrenador!!.eliminarEntrenadorFormulario(
                id.text.toString().toInt()
            )
        }
    }
}