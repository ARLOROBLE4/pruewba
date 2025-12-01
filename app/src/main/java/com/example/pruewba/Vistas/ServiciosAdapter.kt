package com.example.pruewba.Vistas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.R

class ServiciosAdapter(
    private val context: Context,
    private val listaServicios: List<clsServicio>,
    // Eliminamos el listener general de la tarjeta, solo dejamos el del botón
    private val onAgendarClickListener: (clsServicio) -> Unit
) : RecyclerView.Adapter<ServiciosAdapter.ServicioViewHolder>() {

    private val BASE_IMAGE_URL = "https://pcextreme.grupoctic.com/appWeb/aseets/"

    class ServicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgServicio: ImageView = itemView.findViewById(R.id.imgServicio)
        val txtServicio: TextView = itemView.findViewById(R.id.txtServicio)
        val txtInfoServicio: TextView = itemView.findViewById(R.id.txtInfoServicio)
        val btnAgendar: Button = itemView.findViewById(R.id.btnAgendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.servicios_layout, parent, false)
        return ServicioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaServicios.size
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        val servicio = listaServicios[position]

        holder.txtServicio.text = servicio.titulo ?: ""
        holder.txtInfoServicio.text = servicio.descripcion ?: ""

        // Carga de Imagen
        Glide.with(context)
            .load(BASE_IMAGE_URL + servicio.imagen)
            .placeholder(R.drawable.logopcstatus)
            .error(R.drawable.logopcstatus)
            .into(holder.imgServicio)

        // ELIMINADO: holder.itemView.setOnClickListener { ... }
        // Ya no hay acción al hacer clic en la tarjeta vacía.

        // 2. Manejar Clic SOLO en el BOTÓN AGENDAR
        holder.btnAgendar.setOnClickListener {
            onAgendarClickListener(servicio)
        }
    }
}