package umg.edu.gt.progra2.ui.gasto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import umg.edu.gt.progra2.databinding.ActivityGastoBinding
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.ui.Dependencias

class GastoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGastoBinding
    private val control = Dependencias.controlGastos
    private val adapter = GastoAdapter(alTocarGasto = { gasto -> abrirFormulario(gasto) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGastos.layoutManager = LinearLayoutManager(this)
        binding.rvGastos.adapter = adapter

        binding.btnNuevo.setOnClickListener { abrirFormulario(null) }
        binding.btnVolverMenu.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun cargar() {
        lifecycleScope.launch {
            val gastos = control.listar()
            adapter.mostrar(gastos)
            binding.tvResumen.text = control.resumen()
        }
    }

    private fun abrirFormulario(gasto: Gasto?) {
        val intent = Intent(this, FormularioGastoActivity::class.java)
        if (gasto != null) {
            intent.putExtra(FormularioGastoActivity.EXTRA_ID_GASTO, gasto.id)
        }
        startActivity(intent)
    }
}