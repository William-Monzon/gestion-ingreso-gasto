package umg.edu.gt.progra2.util

import java.math.BigDecimal
import java.util.Locale

fun formatearQuetzales(monto: BigDecimal): String =
    String.format(Locale.US, "Q %,.2f", monto)