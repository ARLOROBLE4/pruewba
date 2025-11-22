package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pruewba.R

class perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Enlace de botones para navegación
        val btnInicio = findViewById<Button>(R.id.btnPrfInicio)
        val btnConsulta = findViewById<Button>(R.id.btnPrfConsulta)
        val btnPerfil = findViewById<Button>(R.id.btnPrfPerfil)

        // Implementación del Caso 3: Navegación desde Perfil
        btnInicio.setOnClickListener {
            startActivity(Intent(this, bienvenida::class.java))
            finish()
        }
        btnConsulta.setOnClickListener {
            startActivity(Intent(this, consulta::class.java))
            finish()
        }

        btnPerfil.setOnClickListener {
            // Caso 3: Siempre se queda en Perfil. No se requiere acción de Intent.
        }

        // (Aquí iría la lógica del Presenter/Modelo para cargar los datos del perfil)
    }
}