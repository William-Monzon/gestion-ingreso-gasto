package umg.edu.gt.progra2.modelo.gasto

//Enum para Gastos el cual va en mayúsculas, pero tienen una variable
//nombre visible que indica como mostrarlo en pantalla
enum class CategoriaGasto(val nombreVisible: String) {
    ALIMENTACION("Alimentación"),
    TRANSPORTE("Transporte"),
    ESTUDIOS("Estudios"),
    ENTRETENIMIENTO("Entretenimiento"),
    SALUD("Salud"),
    OTROS("Otros");
    override fun toString(): String = nombreVisible
}