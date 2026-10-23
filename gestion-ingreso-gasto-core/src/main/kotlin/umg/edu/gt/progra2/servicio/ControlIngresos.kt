package umg.edu.gt.progra2.servicio


import umg.edu.gt.progra2.datos.ingreso.IngresoRepositorio
import umg.edu.gt.progra2.modelo.ingreso.CategoriaIngreso
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.util.formatearQuetzales
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate

class ControlIngresos(private val repositorio: IngresoRepositorio,
                    private val reloj: Clock = Clock.systemDefaultZone()) {

    fun validar(ingreso: Ingreso): String? = when {
        ingreso.descripcion.isBlank() -> "La descripción no puede estar vacía"
        ingreso.monto.signum() <= 0 -> "El monto debe ser mayor que 0"
        ingreso.fecha.isAfter(LocalDate.now(reloj)) -> "La fecha del ingreso no puede ser en el futuro"
        else -> null
    }

    suspend fun registrar(ingreso: Ingreso): String? {
        validar(ingreso)?.let { return it }
        repositorio.agregar(ingreso)
        return null
    }

    suspend fun actualizar(ingreso: Ingreso): String? {
        validar(ingreso)?.let { return it }
        return if (repositorio.actualizar(ingreso)) null else "No existe el ingreso #${ingreso.id}"
    }

    suspend fun buscarPorId(id: Int): Ingreso? = repositorio.buscarPorId(id)

    suspend fun listar(): List<Ingreso> =
        repositorio.listar().sortedWith(
            compareByDescending<Ingreso> { it.fecha }.thenByDescending { it.id }
        )

    suspend fun eliminar(id: Int): Boolean = repositorio.eliminar(id)

    suspend fun total(): BigDecimal =
        repositorio.listar().sumOf { it.monto }

    suspend fun totalPorCategoria(): Map<CategoriaIngreso, BigDecimal> =
        repositorio.listar()
            .groupBy { it.categoria }
            .mapValues { (_, ingresos) -> ingresos.sumOf { it.monto } }


    suspend fun resumen(): String {
        val ingresos = repositorio.listar()
        val palabra = if (ingresos.size == 1) "ingreso" else "ingreso"
        return "${ingresos.size} $palabra · Total: ${formatearQuetzales(ingresos.sumOf { it.monto })}"
    }
}
