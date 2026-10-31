package umg.edu.gt.progra2.datos

import androidx.room.Entity
import androidx.room.PrimaryKey

// Un solo Entity, para diferenciar ingresos y gasto se crea el tipo
object TipoMovimiento {
    const val GASTO = "GASTO"
    const val INGRESO = "INGRESO"
}

// Se pasan los atributos a lenguaje de SQLite
@Entity(tableName = "movimientos")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,              // TipoMovimiento.GASTO o TipoMovimiento.INGRESO
    val descripcion: String,
    val montoCentavos: Long,       // 45.50 se guarda como 4550
    val categoria: String,         // nombre del enum (CategoriaGasto o CategoriaIngreso)
    val fechaEpochDay: Long        // LocalDate.toEpochDay()
)