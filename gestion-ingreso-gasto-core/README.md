# gestion-ingreso-gasto-core

Módulo de lógica de negocio en Kotlin puro (JVM), compilado con Maven. No tiene ninguna
dependencia de Android, se puede correr en memoria con la terminal, sin emulador, ni dispositivo.

## Estructura

```
src/main/kotlin/umg/edu/gt/progra2/
├── modelo/
│   ├── Movimiento.kt            <- interfaz común a Gasto e Ingreso
│   ├── Balance.kt               <- totales + balance neto
│   ├── gasto/
│   │   ├── Gasto.kt
│   │   └── CategoriaGasto.kt
│   └── ingreso/
│       ├── Ingreso.kt
│       └── CategoriaIngreso.kt
├── datos/
│   ├── gasto/
│   │   ├── GastoRepositorio.kt           <- interfaz
│   │   └── GastoRepositorioMemoria.kt    <- implementación en memoria (para pruebas)
│   └── ingreso/
│       ├── IngresoRepositorio.kt
│       └── IngresoRepositorioMemoria.kt
├── servicio/
│   ├── ControlGastos.kt
│   ├── ControlIngresos.kt
│   └── ControlBalance.kt
├─── util/
│   └── Extensiones.kt                <- formatearQuetzales(monto)
└── Main.kt                        <- Prueba en consola
```

## Modelo de dominio

### `Movimiento`

Interfaz que implementan  `Gasto` e `Ingreso`, para para tratarlos de forma uniforme
para no tener que repetir código de más:

```
interface Movimiento {
    val id: Int
    val descripcion: String
    val monto: BigDecimal
    val fecha: LocalDate
}
```

### `Ingreso` / `Gasto`

`data class` con los mismos campos base más su propia categoría (`CategoriaGasto` /
`CategoriaIngreso`, enums independientes). El monto es **siempre `BigDecimal`**, nunca
`Double`: con dinero, `Double` acumula errores de redondeo (`0.1 + 0.2 != 0.3` en binario).

La validación de los datos (descripción no vacía, monto positivo, fecha no futura) no vive en el modelo, 
sino en el servicio (`ControlGastos.validar/ControlIngresos.validar`), para tener un solo lugar donde se
decide qué es un movimiento válido.

### `Balance`

Totales de ingresos y gastos, con el balance neto y si es positivo como propiedades
calculadas (`get()`), no guardadas, para que no puedan quedar inconsistentes con los
totales:

```
data class Balance(
    val totalIngresos: BigDecimal,
    val totalGastos: BigDecimal
) {
    val balanceNeto: BigDecimal get() = totalIngresos - totalGastos
    val esPositivo: Boolean get() = balanceNeto.signum() >= 0
 
    companion object {
        fun de(ingresos: List<Ingreso>, gastos: List<Gasto>) = Balance(
            totalIngresos = ingresos.sumOf { it.monto },
            totalGastos = gastos.sumOf { it.monto }
        )
    }
}
```

## Repositorios: interfaz + implementación en memoria

`GastoRepositorio` e `IngresoRepositorio` son interfaces `suspend`. El `core` solo trae una
implementación en memoria (`RepositorioMemoria`), pensada para pruebas rápidas y para
`Main.kt`. La implementación real con una base de datos (Room) vive en el módulo `ui`, que
implementa estas mismas interfaces.

```
interface GastoRepositorio {
    suspend fun agregar(gasto: Gasto): Gasto
    suspend fun listar(): List<Gasto>
    suspend fun buscarPorId(id: Int): Gasto?
    suspend fun actualizar(gasto: Gasto): Boolean
    suspend fun eliminar(id: Int): Boolean
}
```

## Servicios

Son el punto de entrada que usa la `ui`. Validan en el repositorio y ofrecen
operaciones (totales, resumen).

- **`ControlGastos`** / **`ControlIngresos`**: `registrar`, `actualizar`, `eliminar`,
  `listar` (ordenado por fecha descendente), `buscarPorId`, `total`, `totalPorCategoria`, `resumen`.
  `validar(movimiento)` devuelve `String?`: `null` si es válido, o el mensaje de error a
  mostrar.
- **`ControlBalance`**: junta ambos repositorios para calcular el `Balance` general y un


## Instalar este módulo para que lo use la `ui`

Desde la raíz del repositorio:

```
mvn clean install 
```

Esto publica el `.jar` en el repositorio Maven local (`.m2`), que es donde Gradle lo busca cuando 
el proyecto Android declara `mavenLocal()` entre los repositorios.