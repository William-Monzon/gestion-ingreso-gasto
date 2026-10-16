package umg.edu.gt.progra2.modelo

import java.math.BigDecimal
import java.time.LocalDate

interface Movimiento {
    val id: Int
    val descripcion: String
    val monto: BigDecimal
    val fecha: LocalDate
}