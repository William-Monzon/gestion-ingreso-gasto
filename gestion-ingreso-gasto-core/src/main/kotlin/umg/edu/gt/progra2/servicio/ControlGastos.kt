package umg.edu.gt.progra2.servicio

import umg.edu.gt.progra2.datos.gasto.GastoRepositorio
import umg.edu.gt.progra2.modelo.gasto.CategoriaGasto
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.util.formatearQuetzales
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate

//Uso de Clock para no tener problemas con LocalDate()
class ControlGastos(private val repositorio: GastoRepositorio,
                    private val reloj: Clock = Clock.systemDefaultZone()) {

    fun validar(gasto: Gasto): String? = when {
        gasto.descripcion.isBlank() -> "La descripción no puede estar vacía"
        gasto.monto.signum() <= 0 -> "El monto debe ser mayor que 0"
        gasto.fecha.isAfter(LocalDate.now(reloj)) -> "La fecha del gasto no puede ser en el futuro"
        else -> null
    }

    suspend fun registrar(gasto: Gasto): String? {
        validar(gasto)?.let { return it }
        repositorio.agregar(gasto)
        return null
    }

    suspend fun actualizar(gasto: Gasto): String? {
        validar(gasto)?.let { return it }
        return if (repositorio.actualizar(gasto)) null else "No existe el gasto #${gasto.id}"
    }

    suspend fun buscarPorId(id: Int): Gasto? = repositorio.buscarPorId(id)

    suspend fun listar(): List<Gasto> =
        repositorio.listar().sortedWith(
            compareByDescending<Gasto> { it.fecha }.thenByDescending { it.id }
        )

    suspend fun eliminar(id: Int): Boolean = repositorio.eliminar(id)

    suspend fun total(): BigDecimal =
        repositorio.listar().sumOf { it.monto }

    //Agrupa los gastos por categoria y los guarda en un Map en donde el tipo es la clave el valor es el gasto, luego los suma
    suspend fun totalPorCategoria(): Map<CategoriaGasto, BigDecimal> =
        repositorio.listar()
            .groupBy { it.categoria }
            .mapValues { (_, gastos) -> gastos.sumOf { it.monto } }


    suspend fun resumen(): String {
        val gastos = repositorio.listar()
        val palabra = if (gastos.size == 1) "gasto" else "gastos"
        return "${gastos.size} $palabra · Total: ${formatearQuetzales(gastos.sumOf { it.monto })}"
    }
}
