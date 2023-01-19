package com.example.esmnapplication

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador(
    contexto: Context?,
): SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
                CREATE TABLE ENTRENADOR(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOMBRE VARCHAR(50)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {


    }

    fun crearEntrenador(
        nombre:String,
        descripcion: String
    ): Boolean{
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion", descripcion)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "ENTRENADOR",
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if(resultadoGuardar.toInt() == -1) false else true
    }
    fun eliminarEntrenadorFormulario(
        id: Int
    ): Boolean{
        val conexionEscritura = writableDatabase
        /*"""Select * from ENTRENADOR WHERE ID = ?"""
         arrayOf(
            id.toString()
        )*/
        val resultadoEliminacion = conexionEscritura
            .delete(
                "Entrenador",// nombre de tabla
                "id=?",// consulta where
                arrayOf(
                    id.toString()
                )// parametros
            )

        conexionEscritura.close()
        return if(resultadoEliminacion.toInt() == -1) false else true
    }
    fun actualizararEntrenadorFormulario(
        nombre: String,
        descripcion:String,
        idActualizar: Int
    ): Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre",nombre)
        valoresAActualizar.put("descripcion", descripcion)
        val resultadoActualizacion = conexionEscritura
            .update(
                "ENTRENADOR", // NOMBRE DE TABLA
                valoresAActualizar,
                "id=?",
                arrayOf(
                    idActualizar.toString()
                ) // parametros clausura where
            )

        conexionEscritura.close()
        return if(resultadoActualizacion == -1) false else true
    }
    fun consultarEntrenadorPorId(
        id: Int
    ): BEntrenador{
        val baseDeDatosLectura = readableDatabase
        val scriptConsultarUsuario = "Select * FROM ENTRENADOR WHERE ID = ?"
        val resultadoConsultaLectura = baseDeDatosLectura.rawQuery(
            scriptConsultarUsuario,
            arrayOf(
                id.toString()
            )
        )

        // logica de busqueda
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = BEntrenador(0,"","")
        if(existeUsuario){
            do{
                val id = resultadoConsultaLectura.getInt(0) // colimna indice 0 -> ID
                val nombre = resultadoConsultaLectura.getString(1) //columna indice 1 -> nombre
                val descripcion =
                    resultadoConsultaLectura.getString(2)// columna indice 2 - descripcion
                if(id!= null) {
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                }


                }while(resultadoConsultaLectura.moveToNext())

        }

        resultadoConsultaLectura.close()
        baseDeDatosLectura.close()

        return usuarioEncontrado
    }

}