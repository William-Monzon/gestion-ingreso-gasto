package umg.edu.gt.progra2.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import umg.edu.gt.progra2.R
import umg.edu.gt.progra2.databinding.ItemGeneralBinding
import umg.edu.gt.progra2.modelo.Movimiento
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.util.formatearQuetzales

class MovimientoAdapter(
    private val alTocarMovimiento: (Movimiento) -> Unit
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    private var movimientos: List<Movimiento> = emptyList()

    class MovimientoViewHolder(val binding: ItemGeneralBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = movimientos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val binding = ItemGeneralBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = movimientos[position]
        val categoria = when (movimiento) {
            is Gasto -> movimiento.categoria.nombreVisible
            is Ingreso -> movimiento.categoria.nombreVisible
            else -> ""
        }
        val esIngreso = movimiento is Ingreso
        val color = ContextCompat.getColor(
            holder.binding.root.context,
            if (esIngreso) R.color.ingreso_primario else R.color.gasto_primario
        )

        holder.binding.tvDescripcion.text = movimiento.descripcion
        holder.binding.tvDetalle.text = "$categoria · ${movimiento.fecha}"
        holder.binding.tvMonto.text =
            (if (esIngreso) "+ " else "- ") + formatearQuetzales(movimiento.monto)
        holder.binding.tvMonto.setTextColor(color)
        holder.binding.root.setOnClickListener { alTocarMovimiento(movimiento) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun mostrar(nuevos: List<Movimiento>) {
        movimientos = nuevos
        notifyDataSetChanged()
    }
}