package umg.edu.gt.progra2.servicio

import umg.edu.gt.progra2.datos.gasto.GastoRepositorio
import umg.edu.gt.progra2.datos.ingreso.IngresoRepositorio
import umg.edu.gt.progra2.modelo.Balance
import umg.edu.gt.progra2.util.formatearQuetzales

class ControlBalance(
    private val ingresoRepositorio: IngresoRepositorio,
    private val gastoRepositorio: GastoRepositorio
) {

    suspend fun obtenerBalanceGeneral(): Balance =
        Balance.de(ingresoRepositorio.listar(), gastoRepositorio.listar())

    suspend fun resumenGeneral(): String {
        val balance = obtenerBalanceGeneral()
        return "Ingresos: ${formatearQuetzales(balance.totalIngresos)} · " +
                "Gastos: ${formatearQuetzales(balance.totalGastos)} · " +
                "Balance: ${formatearQuetzales(balance.balanceNeto)}"
    }
}