package com.example.pruewba.Vistas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pruewba.Modelo.clsDispositivoHistorial
import com.example.pruewba.R

class HistorialAdapter(
    private val listaDispositivos: List<clsDispositivoHistorial>,
    private val onVerMasClickListener: (clsDispositivoHistorial) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Eliminado: val txtTipoEquipo: TextView = itemView.findViewById(R.id.txtTipoEquipo)
        val txtMarca: TextView = itemView.findViewById(R.id.txtMarca)
        val txtModelo: TextView = itemView.findViewById(R.id.txtModelo)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val btnVerMas: Button = itemView.findViewById(R.id.btnVerMas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.historial_layout, parent, false)
        return HistorialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaDispositivos.size
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val dispositivo = listaDispositivos[position]

        // Eliminado: holder.txtTipoEquipo.text = dispositivo.tipoEquipo
        holder.txtMarca.text = dispositivo.marca
        holder.txtModelo.text = dispositivo.modelo
        holder.txtEstado.text = dispositivo.estado

        holder.btnVerMas.setOnClickListener {
            onVerMasClickListener(dispositivo)
        }
    }
}