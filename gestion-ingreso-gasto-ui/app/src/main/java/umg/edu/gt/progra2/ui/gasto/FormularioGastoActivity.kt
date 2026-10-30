package umg.edu.gt.progra2.ui.gasto

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
import umg.edu.gt.progra2.databinding.ActivityFormularioGastoBinding
import umg.edu.gt.progra2.modelo.gasto.CategoriaGasto
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.ui.Dependencias
import umg.edu.gt.progra2.util.formatearQuetzales
import java.time.LocalDate

class FormularioGastoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioGastoBinding
    private val control = Dependencias.controlGastos

    private var gastoEditando: Gasto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        respetarBarrasDelSistema()

        binding.spCategoria.adapter = ArrayAdapter(
            this, R.layout.simple_spinner_dropdown_item,
            CategoriaGasto.entries.map { it.nombreVisible }
        )

        val id = intent.getIntExtra(EXTRA_ID_GASTO, 0)
        if (id != 0) {
            lifecycleScope.launch { cargarGasto(id) }
        }

        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnEliminar.setOnClickListener { confirmarEliminacion() }
        binding.btnVolver.setOnClickListener { finish() }
    }

    private suspend fun cargarGasto(id: Int) {
        val gasto = control.buscarPorId(id)
        if (gasto == null) {
            Toast.makeText(this, "No existe el gasto #$id", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        gastoEditando = gasto

        binding.tvTitulo.text = "Editar gasto"
        binding.btnGuardar.text = "Guardar cambios"
        binding.btnEliminar.visibility = View.VISIBLE
        binding.etDescripcion.setText(gasto.descripcion)
        binding.etMonto.setText(gasto.monto.toPlainString())
        binding.spCategoria.setSelection(gasto.categoria.ordinal)
    }

    private fun guardar() {
        val monto = binding.etMonto.text.toString().toBigDecimalOrNull()
        if (monto == null) {
            binding.etMonto.error = "Ingresa un monto válido"
            return
        }
        val descripcion = binding.etDescripcion.text.toString().trim()
        val categoria = CategoriaGasto.entries[binding.spCategoria.selectedItemPosition]

        val original = gastoEditando

        lifecycleScope.launch {
            val error = if (original == null) {
                control.registrar(
                    Gasto(
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
                Toast.makeText(this@FormularioGastoActivity, error, Toast.LENGTH_SHORT).show()
                return@launch
            }

            Toast.makeText(this@FormularioGastoActivity, "Gasto guardado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun confirmarEliminacion() {
        val gasto = gastoEditando ?: return
        AlertDialog.Builder(this)
            .setTitle("Eliminar gasto")
            .setMessage("¿Eliminar \"${gasto.descripcion}\" (${formatearQuetzales(gasto.monto)})?\nEsta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> eliminar(gasto.id) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminar(id: Int) {
        lifecycleScope.launch {
            if (control.eliminar(id)) {
                Toast.makeText(this@FormularioGastoActivity, "Gasto eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@FormularioGastoActivity, "No existe el gasto #$id", Toast.LENGTH_SHORT).show()
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
        const val EXTRA_ID_GASTO = "id_gasto"
    }
}