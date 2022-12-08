abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno: Int,
        dos: Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializado")
    }

}
abstract class Numeros(// constructor primario
// public es opcional-defecto
    protected val numeroUno: Int,
    protected val numeroDos: Int
){
    init{
        this.numeroUno
        this.numeroDos
        println("Inicializado")
    }

}
class Suma(
    //Parametros
    uno: Int,
    dos: Int
): Numeros(uno, dos){
    init {
        this.numeroUno
        this.numeroDos
    }
    constructor(
        uno: Int?,
        dos: Int
    ):this(
        if (uno == null) 0 else uno,
        dos
    ){}
    constructor(
        uno: Int,
        dos: Int?
    ):this(
        if (dos == null) 0 else dos,
        uno
    ){}
    constructor(
        uno: Int?,
        dos: Int?
    ):this(
        if (uno == null) 0 else uno,
        if (dos == null) 0 else dos
    ){}
    public fun sumar(): Int{
        return numeroUno + numeroDos
    }
    companion object{ //atributos y metodos compartidos entre instancias
        val pi = 3.14
        fun elevarAlCuadrado(num: Int): Int{
            return num * num
        }
        val historial = arrayListOf<Int>()
        fun agregarHistorias(valorNuevaSuma: Int){
            historial.add(valorNuevaSuma)
        }
    }
}