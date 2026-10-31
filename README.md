# Gestión de Ingresos y Gastos

Esta es una aplicación Android en Kotlin para registrar los ingresos y gastos de una persona y consultar 
su balance general. El proyecto está dividido en dos módulos independientes que se comunican
a través de interfaces que el módulo core definió y el módulo ui implementa.

## Estructura del repositorio

```
gestion-ingreso-gasto/
├── pom.xml                        <- pom padre (Maven)
├── gestion-ingreso-gasto-core/    <- lógica de negocio pura (Kotlin + Maven)
└── gestion-ingreso-gasto-ui/      <- app Android (Kotlin + Gradle)
```

| Módulo | Build system | Se abre con | Qué contien |
|---|---|---|---|
| `gestion-ingreso-gasto-core` | Maven | IntelliJ IDEA | Modelos, repositorios (interfaces), servicios. Sin dependencias de Android. |
| `gestion-ingreso-gasto-ui` | Gradle | Android Studio | Pantallas, Room, Adapters. Consume el `core` como dependencia. |

Cada módulo tien su propio README con la explicación de su contenido:
- [`gestion-ingreso-gasto-core/README.md`](gestion-ingreso-gasto-core/README.md)
- [`gestion-ingreso-gasto-ui/README.md`](gestion-ingreso-gasto-ui/README.md)

## Por qué dos módulos 

El módulo `core` no tiene ninguna dependecia de Android es solo Kotlin puro, se compila con Maven.
La `ui` si necesita de Android Gradle Plugin para poder compilar la app Android.

La `ui` consume al `core` como una dependencia de Maven, esto se instala en un repositorio local
de Maven `.m2` y en dodnde se hace referencia desde `build.gradle.kts` con `mavenLocal()`.

## Cómo se compila y ejecuta

### 1. Instalar el `core` en el repositorio de Maven local

````
mvn clean install 
````

### 2. Abrir la `ui` en Android Studio

Se abrió la carpeta `gestion-ingreso-gasto-ui` como proyecto en Andoid Studio, se espera el sync de Gradle.
En `build.gradle.kts` de `app` se hace referencia al `core` así:

```` 
implementation("umg.edu.gt.progra2:gestion-ingreso-gasto-core:1.0.0-SNAPSHOT")
````

### 3. Ejecutar

Se puede ejecutar ya sea con un emulador o un dispositivo físico (con cable o inalámbrico).

## Flujo de la App en ejecución

1. **Bienvenida / perfil** (`MainActivity`): pide nombre y correo la primera vez, las
   siguientes aperturas van directo al menú.
2. **Menú** (`MenuActivity`): accesos a Gastos, Ingresos, Balance General y edición de perfil.
3. **Gastos / Ingresos** (`GastoActivity` / `IngresoActivity`): crea, lista, actualiza y elimina.
4. **Balance General** (`BalanceActivity`): totales de ingresos, gastos, saldo neto, e
   historial combinado de todos los movimientos.