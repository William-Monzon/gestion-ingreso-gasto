package umg.edu.gt.progra2.datos.ingreso

import umg.edu.gt.progra2.modelo.ingreso.Ingreso

//Class para guardar los ingresos en memoria
//Se usa lambdas en vez de for para recorrer los ingresos
class IngresoRepositorioMemoria : IngresoRepositorio {

    private val ingresos = mutableListOf<Ingreso>()

    private var siguienteId = 1

    override suspend fun agregar(ingreso: Ingreso): Ingreso {
        val guardado = ingreso.copy(id = siguienteId)
        siguienteId++
        ingresos.add(guardado)
        return guardado
    }
    override suspend fun listar(): List<Ingreso> = ingresos.toList()

    override suspend fun buscarPorId(id: Int): Ingreso? {
        return ingresos.find { it.id == id }
    }

    override suspend fun actualizar(ingreso: Ingreso): Boolean {
        val posicion = buscarPosicion(ingreso.id)
        if (posicion == -1) {
            return false
        }
        ingresos[posicion] = ingreso
        return true
    }

    override suspend fun eliminar(id: Int): Boolean {
        return ingresos.removeIf { it.id == id }
    }

    private fun buscarPosicion(id: Int): Int {
        return ingresos.indexOfFirst { it.id == id }
    }
}