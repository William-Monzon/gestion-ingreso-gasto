package umg.edu.gt.progra2.datos.gasto


import umg.edu.gt.progra2.modelo.gasto.Gasto

interface GastoRepositorio {

    suspend fun agregar(gasto: Gasto): Gasto

    suspend fun listar(): List<Gasto>

    suspend fun buscarPorId(id: Int): Gasto?

    suspend fun actualizar(gasto: Gasto): Boolean

    suspend fun eliminar(id: Int): Boolean
}