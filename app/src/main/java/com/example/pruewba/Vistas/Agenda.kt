package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Modelo.agendaModel
import com.example.pruewba.Presentador.AgendaPresenter
import com.example.pruewba.Presentador.Contratos.AgendaContract
import com.example.pruewba.R
import com.google.android.material.progressindicator.CircularProgressIndicator // Si usas este componente

class Agenda : AppCompatActivity(), AgendaContract.View {

    // 🛑 IDs de EditText ajustados para el formulario
    private lateinit var agnNombre: EditText
    private lateinit var agnAPaterno: EditText
    private lateinit var agnAMaterno: EditText
    private lateinit var edtFechaCita: EditText
    private lateinit var edtHora: EditText
    private lateinit var btnGuardarCita: Button // 🛑 Nuevo ID

    private lateinit var txtTituloServicio: TextView // txtConInfo3

    // Elementos de navegación
    private lateinit var btnConInicio4: Button
    private lateinit var btnConPerfil4: Button
    private lateinit var btnConConsulta4: Button

    // Asumo que tienes un componente de carga en el layout raíz
    private var loadingIndicator: View? = null

    private lateinit var presenter: AgendaContract.Presentador
    private var servicioTitulo: String? = null // Para mostrar el servicio

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agenda)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Obtener título del servicio
        servicioTitulo = intent.getStringExtra("servicio_titulo")

        // 2. Mapeo de Vistas
        agnNombre = findViewById(R.id.agnNombre)
        agnAPaterno = findViewById(R.id.agnAPaterno)
        agnAMaterno = findViewById(R.id.agnAMaterno)

        // 🛑 Nuevos IDs de los campos de cita
        edtFechaCita = findViewById(R.id.edtFechaCita)
        edtHora = findViewById(R.id.edtHora)
        btnGuardarCita = findViewById(R.id.btnGuardarCita)

        txtTituloServicio = findViewById(R.id.txtConInfo3)

        btnConInicio4 = findViewById(R.id.btnConInicio4)
        btnConPerfil4 = findViewById(R.id.btnConPerfil4)
        btnConConsulta4 = findViewById(R.id.btnConConsulta4)

        // 3. Configurar UI
        if (servicioTitulo != null) {
            txtTituloServicio.text = "Agendando: $servicioTitulo"
        } else {
            txtTituloServicio.text = "Ingresa los siguientes datos"
        }

        // 4. Inicializar Presenter (Ya NO requiere SesionManager)
        val agendaModel = agendaModel()
        presenter = AgendaPresenter(agendaModel)
        presenter.attachView(this)

        // 5. Listeners
        btnGuardarCita.setOnClickListener {
            presenter.handleGuardarCitaClick()
        }

        btnConInicio4.setOnClickListener { navigateToMainActivity() }
        btnConPerfil4.setOnClickListener { navigateToServicesActivity() }
        btnConConsulta4.setOnClickListener { navigateToHistorialActivity() }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    // --- Implementación de AgendaContract.View ---

    override fun getDatosAgendamiento(): Map<String, String> {
        return mapOf(
            "nombreCitado" to agnNombre.text.toString(),
            "aPaterno" to agnAPaterno.text.toString(),
            "aMaterno" to agnAMaterno.text.toString(),
            "fechaCita" to edtFechaCita.text.toString(),
            "horaCita" to edtHora.text.toString()
        )
    }

    override fun getServicioTitulo(): String = servicioTitulo ?: ""

    override fun showLoading() {
        // Muestra un indicador de carga
        // loadingIndicator?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        // Oculta el indicador de carga
        // loadingIndicator?.visibility = View.GONE
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun clearForm() {
        agnNombre.setText("")
        agnAPaterno.setText("")
        agnAMaterno.setText("")
        edtFechaCita.setText("")
        edtHora.setText("")
    }

    override fun navigateBackToServices() {
        val intent = Intent(this, Servicios::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    // Métodos de navegación auxiliares
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finish()
    }
    private fun navigateToServicesActivity() {
        startActivity(Intent(this, Servicios::class.java))
    }
    private fun navigateToHistorialActivity() {
        startActivity(Intent(this, Historial::class.java))
    }
}