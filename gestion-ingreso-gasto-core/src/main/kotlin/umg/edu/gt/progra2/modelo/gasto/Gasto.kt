package umg.edu.gt.progra2.modelo.gasto

import umg.edu.gt.progra2.modelo.Movimiento
import java.math.BigDecimal
import java.time.LocalDate

data class Gasto(
    override val id: Int = 0,
    override val descripcion: String,
    override val monto: BigDecimal,
    val categoria: CategoriaGasto,
    override val fecha: LocalDate,
) : Movimiento