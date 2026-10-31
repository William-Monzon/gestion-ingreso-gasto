package umg.edu.gt.progra2.datos

import umg.edu.gt.progra2.datos.gasto.GastoRepositorio
import umg.edu.gt.progra2.modelo.gasto.Gasto

// Implementa la inteRfaz de GastoRepositorio para usar el CRUD
class GastoRepositorioRoom(private val dao: MovimientoDao) : GastoRepositorio {

    override suspend fun agregar(gasto: Gasto): Gasto {
        val idNuevo = dao.insertar(gasto.copy(id = 0).toEntity())
        return gasto.copy(id = idNuevo.toInt())
    }

    override suspend fun listar(): List<Gasto> =
        dao.listarPorTipo(TipoMovimiento.GASTO).map { it.toGasto() }

    override suspend fun buscarPorId(id: Int): Gasto? =
        dao.buscarPorId(id, TipoMovimiento.GASTO)?.toGasto()

    override suspend fun actualizar(gasto: Gasto): Boolean {
        // @Update reemplaza por Id sin mirar el tipo, así que primero comprobamos que sea un gasto
        if (dao.buscarPorId(gasto.id, TipoMovimiento.GASTO) == null) return false
        return dao.actualizar(gasto.toEntity()) > 0
    }

    override suspend fun eliminar(id: Int): Boolean =
        dao.eliminar(id, TipoMovimiento.GASTO) > 0
}