package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.pruewba.R

// Esta Activity solo recibe datos del Intent, no necesita Contrato ni Presenter
class ServicioDetalle : AppCompatActivity() {

    private lateinit var imgSrvServicio: ImageView
    private lateinit var txtSrvTitulo: TextView
    private lateinit var txtSrvDescripcion: TextView
    private lateinit var btnSrvAgendar: Button

    // URL base para las imágenes de servicios (debe coincidir con ServiciosAdapter.kt)
    private val BASE_IMAGE_URL = "https://pcextreme.grupoctic.com/appWeb/aseets/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Asegúrate de usar el ID de tu layout de detalle
        setContentView(R.layout.activity_servicio_detalle)

        // El ID del ConstraintLayout raíz del layout de detalle (si lo tuvieras)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Mapeo de Vistas (IDs de activity_servicio_detalle.xml)
        imgSrvServicio = findViewById(R.id.imgSrvServicio)
        txtSrvTitulo = findViewById(R.id.txtSrvTitulo)
        txtSrvDescripcion = findViewById(R.id.txtSrvDescripcion)
        btnSrvAgendar = findViewById(R.id.btnSrvAgendar)

        // Botones de navegación (simplemente para completar la UI, la lógica de navegación se omitirá aquí)
        val btnSrvInicio = findViewById<Button>(R.id.btnSrvInicio)
        val btnSrvServicios = findViewById<Button>(R.id.btnSrvServicios)
        val btnSrvConsulta = findViewById<Button>(R.id.btnSrvConsulta)


        // 2. Obtener datos del Intent
        val servicioTitulo = intent.getStringExtra("titulo")
        val servicioDescripcion = intent.getStringExtra("descripcion")
        val servicioImagen = intent.getStringExtra("imagen")
        // val servicioId = intent.getIntExtra("id", -1) // Si necesitas el ID

        // 3. Cargar Imagen y Texto
        if (servicioImagen != null) {
            Glide.with(this)
                .load(BASE_IMAGE_URL + servicioImagen)
                .into(imgSrvServicio)
        }

        txtSrvTitulo.text = servicioTitulo
        txtSrvDescripcion.text = servicioDescripcion

        // 4. Lógica de Botón (Ejemplo: Agendar)
        btnSrvAgendar.setOnClickListener {
            // Aquí iría la lógica para iniciar la pantalla de agendamiento
            // Por ejemplo: val intent = Intent(this, AgendarActivity::class.java)
            // startActivity(intent)
        }
    }
}