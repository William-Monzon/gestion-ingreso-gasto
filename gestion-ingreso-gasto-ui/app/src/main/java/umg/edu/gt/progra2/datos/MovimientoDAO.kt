package umg.edu.gt.progra2.datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: MovimientoEntity): Long

    @Query("SELECT * FROM movimientos ORDER BY id")
    suspend fun listar(): List<MovimientoEntity>

    @Query("SELECT * FROM movimientos WHERE tipo = :tipo ORDER BY id")
    suspend fun listarPorTipo(tipo: String): List<MovimientoEntity>

    // Siempre con tipo: sin él, "buscar el gasto 3" podría devolver el ingreso 3
    @Query("SELECT * FROM movimientos WHERE id = :id AND tipo = :tipo")
    suspend fun buscarPorId(id: Int, tipo: String): MovimientoEntity?

    @Update
    suspend fun actualizar(movimiento: MovimientoEntity): Int

    @Query("DELETE FROM movimientos WHERE id = :id AND tipo = :tipo")
    suspend fun eliminar(id: Int, tipo: String): Int
}