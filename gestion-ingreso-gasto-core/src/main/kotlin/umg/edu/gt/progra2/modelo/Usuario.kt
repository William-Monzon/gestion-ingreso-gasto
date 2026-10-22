package umg.edu.gt.progra2.modelo

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val correo: String
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(correo.contains("@")) { "El correo debe ser válido" }
    }
}