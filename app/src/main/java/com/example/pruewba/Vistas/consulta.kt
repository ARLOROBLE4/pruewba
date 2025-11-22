package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pruewba.R

class consulta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)

        // Enlace de botones para navegación
        val btnInicio = findViewById<Button>(R.id.btnConInicio)
        val btnConsulta = findViewById<Button>(R.id.btnConConsulta)
        val btnPerfil = findViewById<Button>(R.id.btnConPerfil)

        // Implementación del Caso 2: Navegación desde Consulta
        btnInicio.setOnClickListener {
            startActivity(Intent(this, bienvenida::class.java))
            finish()
        }
        btnConsulta.setOnClickListener {
            // Caso 2: Siempre se queda en Consulta. No se requiere acción de Intent.
        }

        btnPerfil.setOnClickListener {
            startActivity(Intent(this, perfil::class.java))
            finish()
        }

        // (Aquí iría la lógica del Presenter/Modelo para buscar folios, si fuera necesario)
    }
}