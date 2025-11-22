package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.pruewba.R

class bienvenida: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
        val btnInicio = findViewById<Button>(R.id.btnBvnInicio) // Botón "Inicio" en activity_bienvenida.xml
        val btnConsulta = findViewById<Button>(R.id.btnBvnConsulta) // Botón "Consulta" en activity_bienvenida.xml
        val btnPerfil = findViewById<Button>(R.id.btnBvnPerfil) // Botón "Perfil" en activity_bienvenida.xml

        // CASO 1: Lógica de navegación desde Bienvenida

        // 1. Click en Inicio: Muestra siempre activity_bienvenida (se queda aquí)
        btnInicio.setOnClickListener {
            // Se queda en la misma actividad. No se requiere Intent.
        }

        // 2. Click en Consulta: Muestra activity_consulta
        btnConsulta.setOnClickListener {
            val intent = Intent(this, consulta::class.java)
            startActivity(intent)
            finish() // Opcional: cierra la activity actual si quieres limpiar el stack
        }

        // 3. Click en Perfil: Muestra activity_perfil
        btnPerfil.setOnClickListener {
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
            finish() // Opcional: cierra la activity actual si quieres limpiar el stack
        }
    }

}