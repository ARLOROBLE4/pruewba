package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Pruebas.TestApp
import com.example.pruewba.R


class MainActivity : AppCompatActivity() {

    lateinit var btnBvnInicio: Button
    lateinit var btnBvnServicios: Button
    lateinit var btnBvnConsulta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBvnInicio = findViewById(R.id.btnBvnInicio) // Botón "Inicio" en activity_bienvenida.xml
        btnBvnServicios = findViewById(R.id.btnBvnConsulta) // Botón "Servicios" en activity_servicios.xml
        btnBvnConsulta = findViewById(R.id.btnBvnServicios) // Botón "Consulta" en activity_consulta.xml

        btnBvnConsulta.setOnClickListener{

        }
    }
}