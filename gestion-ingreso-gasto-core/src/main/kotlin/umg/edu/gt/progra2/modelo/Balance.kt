package umg.edu.gt.progra2.modelo

import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import java.math.BigDecimal

data class Balance(
    val totalIngresos: BigDecimal,
    val totalGastos: BigDecimal
) {
    val balanceNeto: BigDecimal
        get() = totalIngresos.subtract(totalGastos)

    val esPositivo: Boolean
        get() = balanceNeto.signum() >= 0

    companion object {
        fun de(ingresos: List<Ingreso>, gastos: List<Gasto>) = Balance(
            totalIngresos = ingresos.sumOf { it.monto },
            totalGastos = gastos.sumOf { it.monto }
        )
    }
}