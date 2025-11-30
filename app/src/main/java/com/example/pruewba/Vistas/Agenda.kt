package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts // 🛑 NUEVO: Para permisos
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // 🛑 NUEVO: Para permisos
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Modelo.agendaModel
import com.example.pruewba.Presentador.AgendaPresenter
import com.example.pruewba.Presentador.Contratos.AgendaContract
import com.example.pruewba.R
import android.Manifest // 🛑 NUEVO: Para permisos

class Agenda : AppCompatActivity(), AgendaContract.View {

    private lateinit var agnNombre: EditText
    private lateinit var agnAPaterno: EditText
    private lateinit var agnAMaterno: EditText
    private lateinit var edtFechaCita: EditText
    private lateinit var edtHora: EditText
    private lateinit var agnDetalles: EditText // 🛑 NUEVO: Campo para los detalles
    private lateinit var btnGuardarCita: Button

    private lateinit var txtTituloServicio: TextView

    private lateinit var btnConInicio4: Button
    private lateinit var btnConPerfil4: Button
    private lateinit var btnConConsulta4: Button

    private lateinit var btnDictarDetalles: Button // 🛑 NUEVO: Botón para dictado

    private lateinit var presenter: AgendaContract.Presentador
    private var servicioTitulo: String? = null

    // 🛑 Código de solicitud para el Intent de voz
    private val SPEECH_REQUEST_CODE = 100

    // 🛑 Launcher para solicitar el permiso de micrófono en tiempo de ejecución
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Si el permiso es otorgado, iniciar el dictado
                startVoiceInput()
            } else {
                showToast("El permiso de micrófono es necesario para el dictado de voz.")
            }
        }


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
        edtFechaCita = findViewById(R.id.edtFechaCita)
        edtHora = findViewById(R.id.edtHora)
        btnGuardarCita = findViewById(R.id.btnGuardarCita)
        agnDetalles = findViewById(R.id.agnDetalles) // 🛑 Mapeo del campo de detalles
        btnDictarDetalles = findViewById(R.id.btnDictarDetalles) // 🛑 Mapeo del botón de dictado

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

        // 4. Inicializar Presenter
        val agendaModel = agendaModel()
        presenter = AgendaPresenter(agendaModel)
        presenter.attachView(this)

        // 5. Listeners
        btnGuardarCita.setOnClickListener {
            presenter.handleGuardarCitaClick()
        }

        // 🛑 Listener del Botón de Dictado
        btnDictarDetalles.setOnClickListener {
            checkMicrophonePermission()
        }

        btnConInicio4.setOnClickListener { navigateToMainActivity() }
        btnConPerfil4.setOnClickListener { navigateToServicesActivity() }
        btnConConsulta4.setOnClickListener { navigateToHistorialActivity() }

    }

    // 🛑 Lógica para verificar el permiso del micrófono
    private fun checkMicrophonePermission() {
        when {
            // Permiso ya concedido
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceInput()
            }
            // Explicar por qué se necesita el permiso (opcional)
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                showToast("Necesitamos el permiso del micrófono para grabar tu problema.")
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            // Solicitar permiso por primera vez o si fue denegado sin explicación
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    // 🛑 Inicia el Intent estándar de reconocimiento de voz
    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES") // O el idioma que prefieras
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Describe el problema de tu equipo...")
        }
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            showToast("Tu dispositivo no soporta el dictado de voz.")
        }
    }

    // 🛑 Maneja el resultado del Intent de reconocimiento de voz
    @Deprecated("Usar Activity Result API si es posible, pero forzar para compatibilidad")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results: ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                val spokenText = results[0]
                // 🛑 Añadir el texto dictado al campo agnDetalles
                agnDetalles.setText(spokenText)
            }
        }
    }

    // --- Implementación de AgendaContract.View ---

    override fun getDatosAgendamiento(): Map<String, String> {
        return mapOf(
            "nombreCitado" to agnNombre.text.toString(),
            "aPaterno" to agnAPaterno.text.toString(),
            "aMaterno" to agnAMaterno.text.toString(),
            "fechaCita" to edtFechaCita.text.toString(),
            "horaCita" to edtHora.text.toString(),
            "detalles" to agnDetalles.text.toString() // 🛑 Incluir los detalles en los datos de agendamiento
        )
    }

    override fun getServicioTitulo(): String = servicioTitulo ?: ""

    override fun showLoading() { }
    override fun hideLoading() { }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun clearForm() {
        agnNombre.setText("")
        agnAPaterno.setText("")
        agnAMaterno.setText("")
        edtFechaCita.setText("")
        edtHora.setText("")
        agnDetalles.setText("") // 🛑 Limpiar también el campo de detalles
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