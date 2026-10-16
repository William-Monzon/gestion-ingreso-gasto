package umg.edu.gt.progra2.datos.ingreso

import umg.edu.gt.progra2.modelo.ingreso.Ingreso

interface IngresoRepositorio {

    suspend fun agregar(ingreso: Ingreso): Ingreso

    suspend fun listar(): List<Ingreso>

    suspend fun buscarPorId(id: Int): Ingreso?

    suspend fun actualizar(ingreso: Ingreso): Boolean

    suspend fun eliminar(id: Int): Boolean
}