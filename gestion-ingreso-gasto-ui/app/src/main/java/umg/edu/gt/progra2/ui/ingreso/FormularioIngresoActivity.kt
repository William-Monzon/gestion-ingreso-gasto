package umg.edu.gt.progra2.ui.ingreso

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import umg.edu.gt.progra2.databinding.ActivityFormularioIngresoBinding
import umg.edu.gt.progra2.modelo.ingreso.CategoriaIngreso
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.ui.Dependencias
import umg.edu.gt.progra2.util.formatearQuetzales
import java.time.LocalDate

class FormularioIngresoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioIngresoBinding
    private val control = Dependencias.controlIngresos

    private var ingresoEditando: Ingreso? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioIngresoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        respetarBarrasDelSistema()

        binding.spCategoria.adapter = ArrayAdapter(
            this, R.layout.simple_spinner_dropdown_item,
            CategoriaIngreso.entries.map { it.nombreVisible }
        )

        val id = intent.getIntExtra(EXTRA_ID_INGRESO, 0)
        if (id != 0) {
            lifecycleScope.launch { cargarIngreso(id) }
        }

        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnEliminar.setOnClickListener { confirmarEliminacion() }
        binding.btnVolver.setOnClickListener { finish() }
    }

    private suspend fun cargarIngreso(id: Int) {
        val ingreso = control.buscarPorId(id)
        if (ingreso == null) {
            Toast.makeText(this, "No existe el ingreso #$id", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        ingresoEditando = ingreso

        binding.tvTitulo.text = "Editar ingreso"
        binding.btnGuardar.text = "Guardar cambios"
        binding.btnEliminar.visibility = View.VISIBLE
        binding.etDescripcion.setText(ingreso.descripcion)
        binding.etMonto.setText(ingreso.monto.toPlainString())
        binding.spCategoria.setSelection(ingreso.categoria.ordinal)
    }

    private fun guardar() {
        val monto = binding.etMonto.text.toString().toBigDecimalOrNull()
        if (monto == null) {
            binding.etMonto.error = "Ingresa un monto válido"
            return
        }
        val descripcion = binding.etDescripcion.text.toString().trim()
        val categoria = CategoriaIngreso.entries[binding.spCategoria.selectedItemPosition]

        val original = ingresoEditando

        lifecycleScope.launch {
            val error = if (original == null) {
                control.registrar(
                    Ingreso(
                        descripcion = descripcion,
                        monto = monto,
                        categoria = categoria,
                        fecha = LocalDate.now()
                    )
                )
            } else {
                control.actualizar(
                    original.copy(descripcion = descripcion, monto = monto, categoria = categoria)
                )
            }

            if (error != null) {
                Toast.makeText(this@FormularioIngresoActivity, error, Toast.LENGTH_SHORT).show()
                return@launch
            }

            Toast.makeText(this@FormularioIngresoActivity, "Ingreso guardado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun confirmarEliminacion() {
        val ingreso = ingresoEditando ?: return
        AlertDialog.Builder(this)
            .setTitle("Eliminar ingreso")
            .setMessage("¿Eliminar \"${ingreso.descripcion}\" (${formatearQuetzales(ingreso.monto)})?\nEsta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> eliminar(ingreso.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminar(id: Int) {
        lifecycleScope.launch {
            if (control.eliminar(id)) {
                Toast.makeText(this@FormularioIngresoActivity, "Ingreso eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@FormularioIngresoActivity, "No existe el ingreso #$id", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun respetarBarrasDelSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            vista.updatePadding(top = barras.top, bottom = barras.bottom)
            insets
        }
    }

    companion object {
        const val EXTRA_ID_INGRESO = "id_ingreso"
    }
}