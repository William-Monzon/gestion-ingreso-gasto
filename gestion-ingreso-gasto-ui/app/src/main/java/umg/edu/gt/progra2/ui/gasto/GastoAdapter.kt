package umg.edu.gt.progra2.ui.gasto

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umg.edu.gt.progra2.databinding.ItemGeneralBinding
import umg.edu.gt.progra2.modelo.gasto.Gasto
import umg.edu.gt.progra2.util.formatearQuetzales

class GastoAdapter(
    private val alTocarGasto: (Gasto) -> Unit
) : RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    private var gastos: List<Gasto> = emptyList()

    class GastoViewHolder(val binding: ItemGeneralBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = gastos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val binding = ItemGeneralBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GastoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gasto = gastos[position]
        holder.binding.tvDescripcion.text = gasto.descripcion
        holder.binding.tvDetalle.text = "${gasto.categoria.nombreVisible} · ${gasto.fecha}"
        holder.binding.tvMonto.text = formatearQuetzales(gasto.monto)
        holder.binding.root.setOnClickListener { alTocarGasto(gasto) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun mostrar(nuevos: List<Gasto>) {
        gastos = nuevos
        notifyDataSetChanged()
    }
}