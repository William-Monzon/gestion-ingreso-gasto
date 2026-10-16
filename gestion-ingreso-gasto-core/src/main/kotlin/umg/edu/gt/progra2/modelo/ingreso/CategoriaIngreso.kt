package umg.edu.gt.progra2.modelo.ingreso

enum class CategoriaIngreso(val nombreVisible: String) {
    SUELDO("Sueldo"),
    TAREAS("Tareas"),
    COMISIONES("Comisiones"),
    PROPINAS("Propinas"),
    RENTA("Renta"),
    OTROS("Otros");
    override fun toString(): String = nombreVisible
}