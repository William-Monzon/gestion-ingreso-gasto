package umg.edu.gt.progra2.modelo.gasto

enum class CategoriaGasto(val nombreVisible: String) {
    ALIMENTACION("Alimentación"),
    TRANSPORTE("Transporte"),
    ESTUDIOS("Estudios"),
    ENTRETENIMIENTO("Entretenimiento"),
    SALUD("Salud"),
    OTROS("Otros");
    override fun toString(): String = nombreVisible
}