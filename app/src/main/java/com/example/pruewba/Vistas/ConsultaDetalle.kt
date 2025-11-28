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
import com.example.pruewba.R
// No necesita Presenter ni Contrato, solo muestra datos

class ConsultaDetalle : AppCompatActivity() {

    // Vistas mapeadas de activity_consulta_detalle.xml
    private lateinit var txtCltNumSerie: TextView
    private lateinit var txtCltModelo: TextView
    private lateinit var txtCltMarca: TextView
    private lateinit var txtCltFchRegistro: TextView
    private lateinit var varCltEstado: TextView // Nombre ajustado para evitar ambigüedad en la inicialización
    private lateinit var txtCltDetalles: TextView
    private lateinit var txtCltDiagnostico: TextView
    private lateinit var txtCltCosto: TextView
    private lateinit var imgCltEquipo: ImageView

    // Botones de navegación (simplemente para mapear)
    private lateinit var btnCltInicio: Button
    private lateinit var btnCltConsulta: Button
    private lateinit var btnCltPerfil: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consulta_detalle)

        // Asumiendo que el ConstraintLayout principal tiene el id @+id/main
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Mapeo de Vistas
        txtCltNumSerie = findViewById(R.id.txtCltNumSerie)
        txtCltModelo = findViewById(R.id.txtCltModelo)
        txtCltMarca = findViewById(R.id.txtCltMarca)
        txtCltFchRegistro = findViewById(R.id.txtCltFchRegistro)
        varCltEstado = findViewById(R.id.txtCltEstado) // Usamos el ID de TextView para el Estado
        txtCltDetalles = findViewById(R.id.txtCltDetalles)
        txtCltDiagnostico = findViewById(R.id.txtCltDiagnostico)
        txtCltCosto = findViewById(R.id.txtCltCosto)
        imgCltEquipo = findViewById(R.id.imgCltEquipo)

        // Mapeo de botones de navegación
        btnCltInicio = findViewById(R.id.btnCltInicio)
        btnCltConsulta = findViewById(R.id.btnCltConsulta)
        btnCltPerfil = findViewById(R.id.btnCltPerfil)

        // 2. Obtener datos del Intent (enviados desde Historial.kt)
        val intent = intent

        val numSerie = intent.getStringExtra("numero_serie") ?: "N/D"
        val marca = intent.getStringExtra("marca") ?: "N/D"
        val modelo = intent.getStringExtra("modelo") ?: "N/D"
        val estado = intent.getStringExtra("estado") ?: "N/D"

        // 🛑 Nuevos campos recibidos
        val fechaRegistro = intent.getStringExtra("fecha_ingreso") ?: "N/D"
        val detalles = intent.getStringExtra("detalles") ?: "Sin detalles registrados."
        val diagnostico = intent.getStringExtra("diagnostico") ?: "Pendiente de diagnóstico."
        val costo = intent.getStringExtra("costo") ?: "0.00"

        // 3. Mostrar datos en la UI
        txtCltNumSerie.text = numSerie
        txtCltModelo.text = modelo
        txtCltMarca.text = marca
        varCltEstado.text = estado
        txtCltFchRegistro.text = fechaRegistro
        txtCltDetalles.text = detalles
        txtCltDiagnostico.text = diagnostico
        txtCltCosto.text = costo

        // 4. Configurar Listeners de Navegación
        btnCltInicio.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        }
        btnCltConsulta.setOnClickListener {
            startActivity(Intent(this, Servicios::class.java))
        }
        btnCltPerfil.setOnClickListener {
            startActivity(Intent(this, Historial::class.java))
        }
    }
}