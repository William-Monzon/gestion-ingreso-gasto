package umg.edu.gt.progra2.util

import java.math.BigDecimal
import java.util.Locale

//Las cantidades son en quetzales y con Locale.US se utilizan de forma universal las comas o puntos.
fun formatearQuetzales(monto: BigDecimal): String =
    String.format(Locale.US, "Q %,.2f", monto)