package umg.edu.gt.progra2.ui.ingreso

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import umg.edu.gt.progra2.databinding.ActivityIngresoBinding
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.ui.Dependencias

class IngresoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIngresoBinding
    private val control = Dependencias.controlIngresos
    private val adapter = IngresoAdapter(alTocarIngreso = { ingreso -> abrirFormulario(ingreso) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngresoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvIngresos.layoutManager = LinearLayoutManager(this)
        binding.rvIngresos.adapter = adapter

        binding.btnNuevo.setOnClickListener { abrirFormulario(null) }
        binding.btnVolverMenu.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun cargar() {
        lifecycleScope.launch {
            val ingresos = control.listar()
            adapter.mostrar(ingresos)
            binding.tvResumen.text = control.resumen()
        }
    }

    private fun abrirFormulario(ingreso: Ingreso?) {
        val intent = Intent(this, FormularioIngresoActivity::class.java)
        if (ingreso != null) {
            intent.putExtra(FormularioIngresoActivity.EXTRA_ID_INGRESO, ingreso.id)
        }
        startActivity(intent)
    }
}