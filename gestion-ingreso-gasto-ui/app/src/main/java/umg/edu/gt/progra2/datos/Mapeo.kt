package umg.edu.gt.progra2.datos

import umg.edu.gt.progra2.modelo.gasto.CategoriaGasto
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.modelo.ingreso.CategoriaIngreso
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

private fun BigDecimal.aCentavos(): Long =
    movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact()

fun Gasto.toEntity() = MovimientoEntity(
    id = id,
    tipo = TipoMovimiento.GASTO,
    descripcion = descripcion,
    montoCentavos = monto.aCentavos(),
    categoria = categoria.name,
    fechaEpochDay = fecha.toEpochDay()
)

fun Ingreso.toEntity() = MovimientoEntity(
    id = id,
    tipo = TipoMovimiento.INGRESO,
    descripcion = descripcion,
    montoCentavos = monto.aCentavos(),
    categoria = categoria.name,
    fechaEpochDay = fecha.toEpochDay()
)

fun MovimientoEntity.toGasto() = Gasto(
    id = id,
    descripcion = descripcion,
    monto = BigDecimal.valueOf(montoCentavos, 2),
    categoria = CategoriaGasto.valueOf(categoria),
    fecha = LocalDate.ofEpochDay(fechaEpochDay)
)

fun MovimientoEntity.toIngreso() = Ingreso(
    id = id,
    descripcion = descripcion,
    monto = BigDecimal.valueOf(montoCentavos, 2),
    categoria = CategoriaIngreso.valueOf(categoria),
    fecha = LocalDate.ofEpochDay(fechaEpochDay)
)