package umg.edu.gt.progra2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import umg.edu.gt.progra2.ui.Dependencias
import umg.edu.gt.progra2.databinding.ActivityBalanceBinding
import umg.edu.gt.progra2.modelo.Balance
import umg.edu.gt.progra2.modelo.Movimiento
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.ui.gasto.FormularioGastoActivity
import umg.edu.gt.progra2.ui.ingreso.FormularioIngresoActivity
import umg.edu.gt.progra2.util.formatearQuetzales

class BalanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBalanceBinding
    private val adapter = MovimientoAdapter(alTocarMovimiento = { movimiento -> abrirFormulario(movimiento) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTransacciones.layoutManager = LinearLayoutManager(this)
        binding.rvTransacciones.adapter = adapter

        binding.btnVolverMenu.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun cargar() {
        lifecycleScope.launch {
            val ingresos = Dependencias.controlIngresos.listar()
            val gastos = Dependencias.controlGastos.listar()
            val balance = Balance.de(ingresos, gastos)

            binding.tvSaldoTotal.text = formatearQuetzales(balance.balanceNeto)
            binding.tvTotalIngresos.text = "+ " + formatearQuetzales(balance.totalIngresos)
            binding.tvTotalGastos.text = "- " + formatearQuetzales(balance.totalGastos)

            val movimientos: List<Movimiento> = (ingresos + gastos)
                .sortedWith(compareByDescending<Movimiento> { it.fecha }.thenByDescending { it.id })
            adapter.mostrar(movimientos)
        }
    }

    private fun abrirFormulario(movimiento: Movimiento) {
        val intent = when (movimiento) {
            is Gasto -> Intent(this, FormularioGastoActivity::class.java)
                .putExtra(FormularioGastoActivity.EXTRA_ID_GASTO, movimiento.id)
            is Ingreso -> Intent(this, FormularioIngresoActivity::class.java)
                .putExtra(FormularioIngresoActivity.EXTRA_ID_INGRESO, movimiento.id)
            else -> return
        }
        startActivity(intent)
    }
}