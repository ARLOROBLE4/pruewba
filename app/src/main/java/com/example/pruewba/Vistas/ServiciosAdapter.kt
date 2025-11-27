package com.example.pruewba.Vistas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pruewba.Modelo.clsServicio
import com.example.pruewba.R

class ServiciosAdapter(
    private val context: Context,
    private val listaServicios: List<clsServicio>,
    private val onServiceClickListener: (clsServicio) -> Unit // Listener para el click
) : RecyclerView.Adapter<ServiciosAdapter.ServicioViewHolder>() {

    // URL base para las imágenes de servicios (Ajustar si es necesario)
    private val BASE_IMAGE_URL = "https://pcextreme.grupoctic.com/appWeb/aseets/"

    class ServicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgServicio: ImageView = itemView.findViewById(R.id.imgServicio)
        val txtServicio: TextView = itemView.findViewById(R.id.txtServicio)
        val txtInfoServicio: TextView = itemView.findViewById(R.id.txtInfoServicio)
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

        // 1. Cargar Texto
        holder.txtServicio.text = servicio.titulo
        holder.txtInfoServicio.text = servicio.descripcion

        // 2. Cargar Imagen usando Glide
        Glide.with(context)
            .load(BASE_IMAGE_URL + servicio.imagen)
            .placeholder(R.drawable.logopcstatus) // Placeholder temporal
            .error(R.drawable.logopcstatus) // Imagen de error
            .into(holder.imgServicio)

        // 3. Manejar Click (para ir a Vista Detalle)
        holder.itemView.setOnClickListener {
            onServiceClickListener(servicio)
        }
    }
}