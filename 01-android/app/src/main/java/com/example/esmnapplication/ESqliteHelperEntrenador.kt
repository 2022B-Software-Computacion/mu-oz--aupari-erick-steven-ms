package com.example.esmnapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador(
    contexto: Context?, // THIS
): SQLiteOpenHelper(
    contexto,
    "moviles", // nombres BDD
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
                CREATE TABLE ENTRENADOR(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                descripcion VARCHAR(50)
                )
            """.trimIndent()
        /*val scriptSQLEliminarTablaEntrenador =
            """
                DROP TABLE ENTRENADOR
            """.trimIndent()
        db?.execSQL(scriptSQLEliminarTablaEntrenador)*/
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) { }

    fun crearEntrenador(
        nombre: String,
        descripcion: String
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion", descripcion)
        val resultadoGuardar = baseDatosEscritura
            .insert(
                "ENTRENADOR", //Nombre tabla
                null,
                valoresAGuardar //valores
            )
        baseDatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarEntrenadorFormulario(id: Int): Boolean {
        // val conexionEscritura = this.writableDatabase
        val conexionEscritura = writableDatabase
        // "SELECT * FROM ENTRENADOR WHERE ID = ?"
        // arrayOf(
        //      id.toString()
        // )
        val resultadoEliminacion = conexionEscritura
            .delete(
                "ENTRENADOR", //Nombre tabla
                "id=?", //Clausula where
                arrayOf(
                    id.toString()
                ) //Parametros
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
    }

    fun actualizarEntrenadorFormulario(
        nombre: String,
        descripcion: String,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        val resultadoActualizacion = conexionEscritura
            .update(
                "ENTRENADOR", //Nombre tabla
                valoresAActualizar, //Valores a actualizar
                "id=?", //Clausula where
                arrayOf(
                    idActualizar.toString()
                )   //Paramaetros clausula where
            )
        conexionEscritura.close()
        return if (resultadoActualizacion == -1) false else true
    }

    fun consultarEntrenadorPorId(id: Int): BEntrenador {
        //val baseDatosLectura = this.readableDatabase
        val baseDatosLectura = readableDatabase
        val scriptConsultarUsuario = "SELECT * FROM ENTRENADOR WHERE ID = ?"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario, // Consulta
            arrayOf(
                id.toString()
            )   //Parametros consulta
        )
        //Lógica búsqueda
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        var usuarioEncontrado = BEntrenador(0, "", "")
        // val arreglo = arrayListOf<BEntrenador>()

        if (existeUsuario) {
            do {
                val id = resultadoConsultaLectura.getInt(0) //  columna indice 0 -> ID
                val nombre = resultadoConsultaLectura.getString(1)  // Columna índice 1 -> NOMBRE
                val descripcion =
                    resultadoConsultaLectura.getString(2) // Columna índice 2 -> DESCRIPCION
                if (id != null) {
                    // usuarioEncontrado = BEntrenador(0, "", "")
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                    // arreglo.add(usuarioEncontrado)
                }
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }
}


