package umg.edu.gt.progra2.ui.ingreso

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umg.edu.gt.progra2.databinding.ItemGeneralBinding
import umg.edu.gt.progra2.modelo.ingreso.Ingreso
import umg.edu.gt.progra2.util.formatearQuetzales

class IngresoAdapter(
    private val alTocarIngreso: (Ingreso) -> Unit
) : RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder>() {

    private var ingresos: List<Ingreso> = emptyList()

    class IngresoViewHolder(val binding: ItemGeneralBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = ingresos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngresoViewHolder {
        val binding = ItemGeneralBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngresoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngresoViewHolder, position: Int) {
        val ingreso = ingresos[position]
        holder.binding.tvDescripcion.text = ingreso.descripcion
        holder.binding.tvDetalle.text = "${ingreso.categoria.nombreVisible} · ${ingreso.fecha}"
        holder.binding.tvMonto.text = formatearQuetzales(ingreso.monto)
        holder.binding.root.setOnClickListener { alTocarIngreso(ingreso) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun mostrar(nuevos: List<Ingreso>) {
        ingresos = nuevos
        notifyDataSetChanged()
    }
}