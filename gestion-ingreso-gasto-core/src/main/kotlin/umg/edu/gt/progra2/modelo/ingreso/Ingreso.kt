package umg.edu.gt.progra2.modelo.ingreso

import umg.edu.gt.progra2.modelo.Movimiento
import java.math.BigDecimal
import java.time.LocalDate

data class Ingreso(
    override val id: Int = 0,
    override val descripcion: String,
    override val monto: BigDecimal,
    val categoria: CategoriaIngreso,
    override val fecha: LocalDate
) : Movimiento {
    init {
        require(descripcion.isNotBlank()) { "La descripción no puede estar vacía" }
        require(monto.signum() > 0) { "El monto debe ser mayor a cero" }
    }
}