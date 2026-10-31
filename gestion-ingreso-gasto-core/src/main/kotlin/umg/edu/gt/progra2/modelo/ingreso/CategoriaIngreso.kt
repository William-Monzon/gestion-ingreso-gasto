package umg.edu.gt.progra2.modelo.ingreso

//Enum para Ingresos el cual va en mayúsculas, pero tienen una variable
//nombre visible que indica como mostrarlo en pantalla
enum class CategoriaIngreso(val nombreVisible: String) {
    SUELDO("Sueldo"),
    TAREAS("Tareas"),
    COMISIONES("Comisiones"),
    PROPINAS("Propinas"),
    RENTA("Renta"),
    OTROS("Otros");
    override fun toString(): String = nombreVisible
}