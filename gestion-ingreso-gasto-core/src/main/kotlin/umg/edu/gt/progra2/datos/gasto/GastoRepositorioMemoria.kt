package umg.edu.gt.progra2.datos.gasto

import umg.edu.gt.progra2.modelo.gasto.Gasto

class GastoRepositorioMemoria : GastoRepositorio {

    private val gastos = mutableListOf<Gasto>()

    private var siguienteId = 1

    override suspend fun agregar(gasto: Gasto): Gasto {
        val guardado = gasto.copy(id = siguienteId)
        siguienteId++
        gastos.add(guardado)
        return guardado
    }
    override suspend fun listar(): List<Gasto> = gastos.toList()

    override suspend fun buscarPorId(id: Int): Gasto? {
        return gastos.find { it.id == id }
    }

    override suspend fun actualizar(gasto: Gasto): Boolean {
        val posicion = buscarPosicion(gasto.id)
        if (posicion == -1) {
            return false
        }
        gastos[posicion] = gasto
        return true
    }

    override suspend fun eliminar(id: Int): Boolean {
        return gastos.removeIf { it.id == id }
    }

    private fun buscarPosicion(id: Int): Int {
        return gastos.indexOfFirst { it.id == id }
    }
}
