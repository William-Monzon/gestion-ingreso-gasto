package umg.edu.gt.progra2.modelo

import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import java.math.BigDecimal
import java.math.RoundingMode

data class Balance(
    val totalIngresos: BigDecimal,
    val totalGastos: BigDecimal
) {
    val balanceNeto: BigDecimal
        get() = totalIngresos.subtract(totalGastos)

    val esPositivo: Boolean
        get() = balanceNeto.signum() >= 0
     val diesPorciento: BigDecimal
        get() = totalIngresos.multiply(BigDecimal("0.10").setScale(2, RoundingMode.HALF_UP))

//Companion para instanciar de forma más sencilla
//La suma se realiza con .sumOf() de BigDecimal y en vez de ciclo for para recorrer
//todos los ingresos y gastos se utiliza la expresión lambda.
    companion object {
        fun de(ingresos: List<Ingreso>, gastos: List<Gasto>) = Balance(
            totalIngresos = ingresos.sumOf { it.monto },
            totalGastos = gastos.sumOf { it.monto }
        )
    }
}