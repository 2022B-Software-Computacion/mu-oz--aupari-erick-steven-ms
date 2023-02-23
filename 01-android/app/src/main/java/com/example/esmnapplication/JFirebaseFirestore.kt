package com.example.esmnapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date


class JFirebaseFirestore : AppCompatActivity() {
    var query: Query? = null
    val arreglo: ArrayList<JCitiesDto> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jfirebase_firestore)
        val listview = findViewById<ListView>(R.id.lv_firestore)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listview.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val btnDatosPrueba = findViewById<Button>(R.id.btn_fs_datos_prueba)
        btnDatosPrueba.setOnClickListener { crearDatosPrueba() }

        val btnOrderBy = findViewById<Button>(R.id.btn_fs_order_by)
        btnOrderBy.setOnClickListener {
            consultarConOrderBy(adaptador)
        }

        val btnObtenerDoc = findViewById<Button>(R.id.btn_fs_odoc)
        btnObtenerDoc.setOnClickListener {
            consultarDocumento(adaptador)
        }

        val btnIdCompuesto = findViewById<Button>(R.id.btn_fs_ind_comp)
        btnIdCompuesto.setOnClickListener {
            consultaIndiceCompuesto(adaptador)
        }

        val btnFBCrear = findViewById<Button>(R.id.btn_fs_crear)
        btnFBCrear.setOnClickListener {
            crearDatosEjemplo()
        }

        val btnFBEliminar = findViewById<Button>(R.id.btn_fs_eliminar)
        btnFBEliminar.setOnClickListener {
            eliminarRegistro()
        }

        val btnPag = findViewById<Button>(R.id.btn_fs_epaginar)
        btnPag.setOnClickListener {
            query = null;
            consultarCiudades(adaptador);
        }
    }


    fun consultarCiudades(
        adaptador: ArrayAdapter<JCitiesDto>
    ){
        val db = Firebase.firestore
        val citiesRef = db.collection("cities").orderBy("population").limit(1)

        var tarea: Task<QuerySnapshot>? = null
        if(query == null){
            tarea = citiesRef.get() // primera vez
            limpiarArreglo()
            adaptador.notifyDataSetChanged()
        }else{
            tarea = query!!.get() //consulta de la anterior empezando en el nuevo doc
        }
    }
    fun eliminarRegistro(){
        val db = Firebase.firestore
        val referenceEjemploEstudiante = db.collection("ejemplo")
            .document("123456789").collection("estudiante")

        referenceEjemploEstudiante.document("123456789").delete().addOnSuccessListener {  }.addOnFailureListener{}
    }


    fun crearDatosEjemplo(){
        val db = Firebase.firestore
        val referenciaEjemploEstudiante = db.collection("ejemplo")
            .document("123456789")
            .collection("estudiante")
        val identificador = Date().time
        val datosEstudiante = hashMapOf(
            "nombre" to "Erick",
            "graduado" to false,
            "promedio" to 14.00,
            "direccion" to hashMapOf(
                "direccion" to "mitad del mundo",
                "numeroCalle" to 1234
            ),
            "materias" to listOf("web", "moviles")
        )
        referenciaEjemploEstudiante
            .document("123456789")
            .set(datosEstudiante)
            .addOnSuccessListener { /*si salio bien*/}
            .addOnFailureListener{ /* sale mal */}

        referenciaEjemploEstudiante
            .document(identificador.toString())
            .set(datosEstudiante)
            .addOnSuccessListener { /*si salio bien*/}
            .addOnFailureListener{ /* sale mal */}
        referenciaEjemploEstudiante
            .add(datosEstudiante)
            .addOnSuccessListener { /*si salio bien*/}
            .addOnFailureListener{ /* sale mal */}
    }




    fun consultarConOrderBy(
        adaptador: ArrayAdapter<JCitiesDto>
    ){
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        citiesRefUnico
        //no usamos con docu xq en doc nos devuelve 1
        // cities population
            .orderBy("population", Query.Direction.ASCENDING)
            .get() // obtenemos la peticion
            .addOnSuccessListener {
                for (ciudad in it){
                    anadirArregloCiudad(arreglo,ciudad,adaptador)
                }
            }
    }


    fun consultaIndiceCompuesto(
        adaptador: ArrayAdapter<JCitiesDto>
    ){
        val db = Firebase.firestore
        val citiesRefUnico = db
            .collection("cities")
        citiesRefUnico.whereEqualTo("capital",false)
            .whereLessThanOrEqualTo("population", 4000000)
            .orderBy("population", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (ciudad in it){
                    anadirArregloCiudad(arreglo, ciudad, adaptador)
                }
            }
    }

    fun consultarDocumento(
        adaptador: ArrayAdapter<JCitiesDto>
    ){
        val db = Firebase.firestore
        val citiesRefUnico = db
            .collection("cities")
        // 1 doc
        citiesRefUnico.document("BJ")
            .get().addOnSuccessListener {
                limpiarArreglo()
                arreglo.add(
                    JCitiesDto(
                        it.data?.get("name") as String?,
                        it.data?.get("state") as String?,
                        it.data?.get("country") as String?,
                        it.data?.get("capital") as Boolean?,
                        it.data?.get("population") as Long?,
                        it.data?.get("regions") as ArrayList<String>
                     )
                )
            }
    }

    fun crearDatosPrueba(){
        val db = Firebase.firestore
        val cities = db.collection("cities")

        val data1 = hashMapOf(
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 860000,
            "regions" to listOf("west_coast", "norcal")
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "socal")
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C.",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast")
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu")
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").set(data5)
    }

    fun limpiarArreglo(){
        arreglo.clear()
    }

    fun anadirArregloCiudad(
        arregloNuevo: ArrayList<JCitiesDto>,
        ciudad: QueryDocumentSnapshot,
        adaptador: ArrayAdapter<JCitiesDto>
    ){
        val nuevaCiudad = JCitiesDto(
            ciudad.data.get("name") as String?,
            ciudad.data.get("state") as String?,
            ciudad.data.get("country") as String?,
            ciudad.data.get("capital") as Boolean?,
            ciudad.data.get("population") as Long?,
            ciudad.data.get("regions") as ArrayList<String>

            )
        arregloNuevo.add(
            nuevaCiudad
        )
        adaptador.notifyDataSetChanged()
    }
}