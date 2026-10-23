package umg.edu.gt.progra2

import umg.edu.gt.progra2.datos.gasto.GastoRepositorioMemoria
import umg.edu.gt.progra2.datos.ingreso.IngresoRepositorioMemoria
import umg.edu.gt.progra2.modelo.gasto.CategoriaGasto
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.modelo.ingreso.CategoriaIngreso
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.servicio.ControlGastos
import umg.edu.gt.progra2.servicio.ControlIngresos
import umg.edu.gt.progra2.util.formatearQuetzales
import java.math.BigDecimal
import java.time.LocalDate


suspend fun main()  {
    val controlGastos = ControlGastos(GastoRepositorioMemoria())
    val controlIngresos = ControlIngresos(IngresoRepositorioMemoria())

    controlIngresos.registrar(
        Ingreso(descripcion = "Salario quincenal", monto = BigDecimal("3500.00"), categoria = CategoriaIngreso.SUELDO, fecha = LocalDate.of(2026, 10, 1))
    )
    controlGastos.registrar(
        Gasto(descripcion = "Almuerzo", monto = BigDecimal("45.50"), categoria = CategoriaGasto.ALIMENTACION, fecha = LocalDate.of(2026, 10, 3))
    )

    while (true) {
        val totalIngresos = controlIngresos.total()
        val totalGastos = controlGastos.total()
        val saldoDisponible = totalIngresos.subtract(totalGastos)


        println("=== CONTROL FINANCIERO (CONSOLA) ===")
        println("Saldo Disponible: ${formatearQuetzales(saldoDisponible)}")
        println("1. Registrar Ingreso")
        println("2. Registrar Gasto")
        println("3. Listar Transacciones")
        println("4. Ver Balance General")
        println("5. Actualizar un Registro")
        println("6. Eliminar un Registro")
        println("0. Salir")
        print("Seleccione una opción: ")

        when (readlnOrNull()?.trim()) {
            "1" -> registrarIngreso(controlIngresos)
            "2" -> registrarGasto(controlGastos)
            "3" -> listarTransacciones(controlIngresos, controlGastos)
            "4" -> mostrarBalance(controlIngresos, controlGastos)
            "5" -> actualizarRegistro(controlIngresos, controlGastos)
            "6" -> eliminarRegistro(controlIngresos, controlGastos)
            "0", null -> {
                println("¡Hasta pronto!")
                return
            }
            else -> println("Opción no válida.")
        }
    }
}

private suspend fun registrarIngreso(control: ControlIngresos) {
    registrarMovimiento(
        titulo = "REGISTRAR INGRESO",
        categorias = CategoriaIngreso.entries
    ) { descripcion, monto, categoria, fecha ->
        control.registrar(Ingreso(descripcion = descripcion, monto = monto, categoria = categoria as CategoriaIngreso, fecha = fecha))
    }
}

private suspend fun registrarGasto(control: ControlGastos) {
    registrarMovimiento(
        titulo = "REGISTRAR GASTO",
        categorias = CategoriaGasto.entries
    ) { descripcion, monto, categoria, fecha ->
        control.registrar(Gasto(descripcion = descripcion, monto = monto, categoria = categoria as CategoriaGasto, fecha = fecha))
    }
}

private suspend fun listarTransacciones(controlIngresos: ControlIngresos, controlGastos: ControlGastos) {
    println("\n--- HISTORIAL ---")
    val ingresos = controlIngresos.listar()
    val gastos = controlGastos.listar()

    println("\n[INGRESOS]")
    for (ingreso in ingresos) {
        println("  (+) ID:${ingreso.id} | ${ingreso.fecha} | ${ingreso.descripcion} (${ingreso.categoria}) | ${formatearQuetzales(ingreso.monto)}")
    }

    println("\n[GASTOS]")
    for (gasto in gastos) {
        println("  (-) ID:${gasto.id} | ${gasto.fecha} | ${gasto.descripcion} (${gasto.categoria}) | ${formatearQuetzales(gasto.monto)}")
    }
}

private suspend fun mostrarBalance(controlIngresos: ControlIngresos, controlGastos: ControlGastos) {
    val totalIngresos = controlIngresos.total()
    val totalGastos = controlGastos.total()
    val saldo = totalIngresos.subtract(totalGastos)


    println("--------  BALANCE GENERAL  --------")
    println(" Total Ingresos : + ${formatearQuetzales(totalIngresos)}")
    println(" Total Gastos   : - ${formatearQuetzales(totalGastos)}")
    println("----------------------------------------")
    println(" Saldo Neto     :   ${formatearQuetzales(saldo)}")

}
private suspend fun actualizarRegistro(controlIngresos: ControlIngresos, controlGastos: ControlGastos) {
    print("¿Qué desea actualizar? (1. Ingreso / 2. Gasto): ")
    when (readlnOrNull()?.trim()) {
        "1" -> {
            print("Ingrese el ID del Ingreso que desea actualizar: ")
            val id = leerIdValido() ?: return

            val ingresoExistente = controlIngresos.buscarPorId(id)
            if (ingresoExistente == null) {
                println("ERROR: No existe un ingreso con el ID #$id.")
                return
            }

            actualizarCampos(
                nombre = "Ingreso",
                descripcionActual = ingresoExistente.descripcion,
                montoActual = ingresoExistente.monto,
                guardar = { nuevaDescripcion, nuevoMonto ->
                    controlIngresos.actualizar(ingresoExistente.copy(descripcion = nuevaDescripcion, monto = nuevoMonto))
                }
            )
        }
        "2" -> {
            print("Ingrese el ID del Gasto que desea actualizar: ")
            val id = leerIdValido() ?: return

            val gastoExistente = controlGastos.buscarPorId(id)
            if (gastoExistente == null) {
                println("ERROR: No existe un gasto con el ID #$id.")
                return
            }

            actualizarCampos(
                nombre = "Gasto",
                descripcionActual = gastoExistente.descripcion,
                montoActual = gastoExistente.monto,
                guardar = { nuevaDescripcion, nuevoMonto ->
                    controlGastos.actualizar(gastoExistente.copy(descripcion = nuevaDescripcion, monto = nuevoMonto))
                }
            )
        }
        else -> println("Opción inválida.")
    }
}

private suspend fun eliminarRegistro(controlIngresos: ControlIngresos, controlGastos: ControlGastos) {
    print("¿Qué desea eliminar? (1. Ingreso / 2. Gasto): ")
    when (readlnOrNull()?.trim()) {
        "1" -> {
            print("ID del Ingreso: ")
            val id = readlnOrNull()?.trim()?.toIntOrNull() ?: return println("ID inválido.")
            if (controlIngresos.eliminar(id)) println("OK -> Ingreso eliminado.")
            else println("ERROR: No existe.")
        }
        "2" -> {
            print("ID del Gasto: ")
            val id = readlnOrNull()?.trim()?.toIntOrNull() ?: return println("ID inválido.")
            if (controlGastos.eliminar(id)) println("OK -> Gasto eliminado.")
            else println("ERROR: No existe.")
        }
        else -> println("Opción inválida.")
    }
}

private suspend fun registrarMovimiento(
    titulo: String,
    categorias: List<Any>,
    onRegistrar: suspend (descripcion: String, monto: BigDecimal, categoria: Any, fecha: LocalDate) -> String?
) {
    println("\n--- $titulo ---")
    print("Descripción: ")
    val descripcion = readlnOrNull()?.trim() ?: ""

    print("Monto (Q): ")
    val monto = readlnOrNull()?.trim()?.toBigDecimalOrNull()
    if (monto == null) {
        println("ERROR: Monto inválido.")
        return
    }

    for ((i, categoria) in categorias.withIndex()) {
        println("  ${i + 1}. $categoria")
    }
    print("Elija categoría: ")
    val numCategoria = readlnOrNull()?.trim()?.toIntOrNull()
    if (numCategoria == null || numCategoria !in 1..categorias.size) {
        println("ERROR: Categoría no válida.")
        return
    }
    val categoria = categorias[numCategoria - 1]

    val fecha = LocalDate.now()

    val error = onRegistrar(descripcion, monto, categoria, fecha)
    if (error == null) {
        println("Registrado correctamente (Fecha: $fecha).")
    } else {
        println("ERROR: $error")
    }
}

private fun leerIdValido(): Int? {
    val id = readlnOrNull()?.trim()?.toIntOrNull()
    if (id == null) {
        println("ERROR: ID inválido.")
    }
    return id
}

private suspend fun actualizarCampos(
    nombre: String,
    descripcionActual: String,
    montoActual: BigDecimal,
    guardar: suspend (String, BigDecimal) -> String?
) {
    println("Actualizando $nombre ($descripcionActual)")

    print("Nueva descripción ($descripcionActual): ")
    val nuevaDescripcion = readlnOrNull()?.trim().let { if (it.isNullOrEmpty()) descripcionActual else it }

    print("Nuevo monto (Q$montoActual): ")
    val monto = readlnOrNull()?.trim()
    val nuevoMonto = if (monto.isNullOrEmpty()) montoActual else monto.toBigDecimalOrNull()
    if (nuevoMonto == null) {
        println("ERROR: Monto inválido.")
        return
    }

    val error = guardar(nuevaDescripcion, nuevoMonto)
    if (error == null) {
        println("$nombre actualizado correctamente.")
    } else {
        println("ERROR: $error")
    }
}
