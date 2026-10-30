package umg.edu.gt.progra2.ui

import android.content.Context
import umg.edu.gt.progra2.datos.AppDatabase
import umg.edu.gt.progra2.datos.GastoRepositorioRoom
import umg.edu.gt.progra2.datos.IngresoRepositorioRoom
import umg.edu.gt.progra2.servicio.ControlBalance
import umg.edu.gt.progra2.servicio.ControlGastos
import umg.edu.gt.progra2.servicio.ControlIngresos

/**
 * Las piezas que TODA la app comparte (un solo objeto para todas las pantallas).
 * Se inicia UNA vez desde la clase Application, antes de que se abra cualquier pantalla.
 */
object Dependencias {

    lateinit var controlGastos: ControlGastos
        private set

    lateinit var controlIngresos: ControlIngresos
        private set

    lateinit var controlBalance: ControlBalance
        private set

    fun iniciar(context: Context) {
        // Una sola base de datos y un solo DAO para gastos e ingresos
        val dao = AppDatabase.obtener(context).movimientoDao()

        val gastos = GastoRepositorioRoom(dao)
        val ingresos = IngresoRepositorioRoom(dao)

        controlGastos = ControlGastos(gastos)
        controlIngresos = ControlIngresos(ingresos)
        controlBalance = ControlBalance(ingresos, gastos)
    }
}