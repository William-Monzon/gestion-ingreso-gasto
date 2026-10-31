package umg.edu.gt.progra2.modelo

import java.math.BigDecimal
import java.time.LocalDate

//Interfaz para que tanto Ingresos como Gastos la utilicen porque ambos comparten los mismos atributos
interface Movimiento {
    val id: Int
    val descripcion: String
    val monto: BigDecimal
    val fecha: LocalDate
}