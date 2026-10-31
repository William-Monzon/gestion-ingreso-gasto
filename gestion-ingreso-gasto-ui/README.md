# gestion-ingreso-gasto-ui

App Android (Kotlin + Gradle + Views/XML) que consume el módulo
[`gestion-ingreso-gasto-core`](../gestion-ingreso-gasto-core/README.md) como dependencia
Maven y persiste los datos con Room.

## Requisito previo

Este módulo no compila solo, necesita que el `core` esté instalado en el repositorio Maven
local.

## Estructura

```
app/src/main/java/umg/edu/gt/progra2/
├── App.kt                          <- Application; inicia Dependencias al arrancar
├── Dependencias.kt                 <- arma repositorios y servicios, una sola vez
├── datos/
│   ├── TipoMovimiento.kt           <- constantes "GASTO" / "INGRESO"
│   ├── MovimientoEntity.kt         <- @Entity de Room (una sola tabla)
│   ├── MovimientoDao.kt            <- @Dao compartido por gastos e ingresos
│   ├── AppDatabase.kt              <- @Database, instancia única 
│   ├── Mapeo.kt                    <- conversión core <-> Room
│   ├── GastoRepositorioRoom.kt     <- implementa GastoRepositorio del core
│   └── IngresoRepositorioRoom.kt   <- implementa IngresoRepositorio del core
└── ui/
    ├── Prefs.kt                    <- claves de SharedPreferences centralizadas
    ├── MainActivity.kt             <- bienvenida / editar perfil
    ├── MenuActivity.kt             <- menú principal
    ├── GastoActivity.kt / IngresoActivity.kt       <- listas
    ├── FormularioGastoActivity.kt / FormularioIngresoActivity.kt  <- registra/actualiza/elimina
    ├── BalanceActivity.kt          <- balance general + historial combinado
    ├── GastoAdapter.kt / IngresoAdapter.kt         <- RecyclerView.Adapter por tipo
    └── MovimientoAdapter.kt        <- adapter mezclado (usa la interfaz Movimiento)
```

## Persistencia con una sola tabla para gastos e ingresos

En vez de dos tablas separadas, `MovimientoEntity` guarda ambos tipos en una tabla
`movimientos`, distinguidos por la columna `tipo` (`"GASTO"` / `"INGRESO"`). Un único `MovimientoDao` sirve a los dos repositorios.

Room no sabe persistir `BigDecimal` ni `LocalDate` directamente, así que la entity usa tipos
simples y `Mapeo.kt` hace la conversión:

| Core (`Gasto`/`Ingreso`) | `MovimientoEntity` | Conversión |
|---|---|---|
| `monto: BigDecimal` | `montoCentavos: Long` | `movePointRight(2)` al guardar · `BigDecimal.valueOf(c, 2)` al leer |
| `fecha: LocalDate` | `fechaEpochDay: Long` | `toEpochDay()` · `LocalDate.ofEpochDay(...)` |
| `categoria: CategoriaGasto/Ingreso` | `categoria: String` | `.name` · `.valueOf(...)` |

`GastoRepositorioRoom` e `IngresoRepositorioRoom` reciben el **mismo** `MovimientoDao` y cada
uno filtra por su `tipo`, implementando las interfaces `GastoRepositorio` / `IngresoRepositorio`
que define el `core`. El `core` no sabe que Room existe.

## `Dependencias` es un único punto de ensamblado

`object Dependencias` arma una sola vez los repositorios y servicios, y las pantallas los usan
sin crear sus propias instancias:

```
object Dependencias {
    lateinit var controlGastos: ControlGastos
        private set
    lateinit var controlIngresos: ControlIngresos
        private set
    lateinit var controlBalance: ControlBalance
        private set
 
    fun iniciar(context: Context) {
        val dao = AppDatabase.obtener(context).movimientoDao()
        val gastos = GastoRepositorioRoom(dao)
        val ingresos = IngresoRepositorioRoom(dao)
        controlGastos = ControlGastos(gastos)
        controlIngresos = ControlIngresos(ingresos)
        controlBalance = ControlBalance(ingresos, gastos)
    }
}
```

## Flujo de pantallas

```
MainActivity (bienvenida / perfil)
        │  primera vez: pide nombre y correo
        │  siguientes veces: salta directo
        ▼
MenuActivity
        ├──> GastoActivity ──> FormularioGastoActivity (crear / editar / eliminar)
        ├──> IngresoActivity ──> FormularioIngresoActivity (crear / editar / eliminar)
        ├──> BalanceActivity (totales + historial combinado)
        └──> MainActivity en modo edición (botón "Configuración de Perfil")
```

Cada lista recarga sus datos en `onResume()`, así que al volver de crear/editar/eliminar un
movimiento, siempre se ve el estado actualizado sin lógica adicional.

## Los tres adapters

| Adapter | Lista que recibe | Dónde se usa |
|---|---|---|
| `GastoAdapter` | `List<Gasto>` | `GastoActivity` |
| `IngresoAdapter` | `List<Ingreso>` | `IngresoActivity` |
| `MovimientoAdapter` | `List<Movimiento>` | `BalanceActivity` (gastos e ingresos mezclados, ordenados por fecha) |

`MovimientoAdapter` es posible gracias a que `Ingreso` y `Gasto`  implementan la interfaz
`Movimiento` del `core`, adentro distingue el tipo con `is Gasto` / `is Ingreso` para elegir
color y a qué formulario llevar al tocar una fila.

Los tres reutilizan el mismo layout de fila: `item_general.xml`.

## `Prefs`: claves de SharedPreferences centralizadas

Evita escribir las claves como texto suelto en más de un archivo (`MainActivity` y
`MenuActivity` comparten las mismas):

```
object Prefs {
    const val ARCHIVO = "AppPreferences"
    const val NOMBRE = "NOMBRE_USUARIO"
    const val CORREO = "CORREO_USUARIO"
    const val MODO_EDICION = "MODO_EDICION"
}
```

## Dependencias de Gradle relevantes

- `androidx.room:room-runtime` + `ksp(androidx.room:room-compiler)` — persistencia.
- `androidx.recyclerview:recyclerview` — listas.
- `androidx.lifecycle:lifecycle-runtime-ktx` — `lifecycleScope` para lanzar corrutinas desde
  las Activities.
- `com.google.android.material:material` — `MaterialCardView`, `TextInputLayout`, etc.
- `umg.edu.gt.progra2:gestion-ingreso-gasto-core:1.0.0-SNAPSHOT` — el módulo `core`, resuelto
  vía `mavenLocal()`.