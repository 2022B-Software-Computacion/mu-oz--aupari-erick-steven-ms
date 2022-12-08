import java.util.*
import kotlin.collections.ArrayList

fun main(){
    println("Hola Mundo")
    // Tipos de variables


    // INMUTABLES (No se puede reasignar)
    val inmutable: String = "Erick"
    // MUTABLES
    var mutable: String = "Steven"
    mutable = "pedro"

    //println(mutable)

    // val > var


    //Sintaxis Duck Typing

    val ejemploVariable = "Ejemplo"
    val edadEjemplo: Int = 12
    ejemploVariable.trim()

    //variables primitivas
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'S'
    val mayorEdad: Boolean = true
    //Clases de JAVA
    val fechaNacimiento: Date = Date()


    //condicionales

    if(true){

    }else{

    }

    //switch no existe

    val estadoCivilWhen = "S"
    when (estadoCivilWhen){
        ("S") ->{
            println("acercarse")
        }
        "C" -> {
            println("alejarse")
        }
        "UN" -> println("hablar")
        else -> println("no reconocido")

    }

    val coqueteo = if (estadoCivilWhen == "S") "SI" else "NO"


    fun imprimirNombre(nombre: String): Unit{
        println("Nombre: ${nombre}")
    }

    fun calcularSueldo(
        sueldo: Double, // Requerido
        tasa: Double = 12.00, // Opcional (Defecto)
        bono: Double? = null, // Opcional (NULL) -> nullable
    ): Double {
        // String -> String?
        // Int -> Int?
        // Date -> Date?
        if (bono == null){
            return sueldo*(100/tasa)
        }else{
            return sueldo*(100/tasa) * bono
        }
    }

    //println(coqueteo)
    val sumaUno = Suma(1,1)
    val sumaDos = Suma(null,1)
    val sumaTres = Suma(1,null)
    val sumaCuatro = Suma(null,null)

    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()
    println(Suma.historial)

    //Arreglo Estatico
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)

    //Arreglo Dinamico
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add( 12)
    // Operadores funcionan para ambos tipos de arreglos
    println(arregloDinamico)

    // for each -Unit
    // iterar un arreglo

    val respuesta: Unit = arregloDinamico.forEach{
        valorActual: Int ->
        println("Valor actual:${valorActual}")
    }
    arregloEstatico.forEachIndexed{
        indice: Int, valorActual: Int ->
        println("Valor ${valorActual} Indice: ${indice}")
    }
    println(respuesta)

    val respuestaMapDos = arregloDinamico.map{ it + 15}
    println(respuestaMapDos)
}

