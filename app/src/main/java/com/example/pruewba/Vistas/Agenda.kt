package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.R

// Nota: Esta es una Vista simple que recibe datos del servicio para el agendamiento.
class Agenda : AppCompatActivity() {

    private lateinit var agnNombre: EditText
    private lateinit var agnAPaterno: EditText
    private lateinit var agnAMaterno: EditText
    private lateinit var agnFecha: EditText
    private lateinit var txtTituloServicio: TextView // txtConInfo3

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agenda)

        // Asumiendo que el ConstraintLayout principal tiene el id @+id/main
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mapeo de Vistas del activity_agenda.xml
        agnNombre = findViewById(R.id.agnNombre)
        agnAPaterno = findViewById(R.id.agnAPaterno)
        agnAMaterno = findViewById(R.id.agnAMaterno)
        agnFecha = findViewById(R.id.agnFecha)
        txtTituloServicio = findViewById(R.id.txtConInfo3)

        // Opcional: Mostrar el servicio que se está agendando
        val servicioTitulo = intent.getStringExtra("servicio_titulo")
        if (servicioTitulo != null) {
            // Se usa el título de la Activity como título principal
            txtTituloServicio.text = "Agendando: $servicioTitulo"
        } else {
            txtTituloServicio.text = "Ingresa los siguientes datos"
        }
    }
}