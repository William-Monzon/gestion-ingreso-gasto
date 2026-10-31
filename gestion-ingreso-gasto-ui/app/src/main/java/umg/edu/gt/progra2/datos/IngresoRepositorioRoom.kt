package umg.edu.gt.progra2.datos

import umg.edu.gt.progra2.datos.ingreso.IngresoRepositorio
import umg.edu.gt.progra2.modelo.ingreso.Ingreso

// Implementa la inteRfaz de IngresoRepositorio para usar el CRUD
class IngresoRepositorioRoom(private val dao: MovimientoDao) : IngresoRepositorio {

    override suspend fun agregar(ingreso: Ingreso): Ingreso {
        val idNuevo = dao.insertar(ingreso.copy(id = 0).toEntity())
        return ingreso.copy(id = idNuevo.toInt())
    }

    override suspend fun listar(): List<Ingreso> =
        dao.listarPorTipo(TipoMovimiento.INGRESO).map { it.toIngreso() }

    override suspend fun buscarPorId(id: Int): Ingreso? =
        dao.buscarPorId(id, TipoMovimiento.INGRESO)?.toIngreso()

    override suspend fun actualizar(ingreso: Ingreso): Boolean {
        if (dao.buscarPorId(ingreso.id, TipoMovimiento.INGRESO) == null) return false
        return dao.actualizar(ingreso.toEntity()) > 0
    }

    override suspend fun eliminar(id: Int): Boolean =
        dao.eliminar(id, TipoMovimiento.INGRESO) > 0
}