package com.example.pruewba.Vistas

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter // Importante
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner // Importante
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruewba.Modelo.agendaModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Presentador.AgendaPresenter
import com.example.pruewba.Presentador.Contratos.AgendaContract
import com.example.pruewba.R
import android.Manifest
import java.util.Calendar
import android.graphics.Color
import android.view.View
import android.view.ViewGroup

class Agenda : AppCompatActivity(), AgendaContract.View {

    private lateinit var agnNombre: EditText
    private lateinit var agnAPaterno: EditText
    private lateinit var agnAMaterno: EditText
    private lateinit var edtFechaCita: EditText
    private lateinit var spnHora: Spinner // CAMBIO: Ahora es Spinner
    private lateinit var agnDetalles: EditText
    private lateinit var btnGuardarCita: Button

    private lateinit var txtTituloServicio: TextView

    private lateinit var btnConInicio4: Button
    private lateinit var btnConPerfil4: Button
    private lateinit var btnConConsulta4: Button

    private lateinit var btnDictarDetalles: Button

    private lateinit var presenter: AgendaContract.Presentador
    private lateinit var sessionManager: SesionManager
    private var servicioTitulo: String? = null

    private val SPEECH_REQUEST_CODE = 100

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
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

        sessionManager = SesionManager(this)
        servicioTitulo = intent.getStringExtra("servicio_titulo")

        // Mapeo de Vistas
        agnNombre = findViewById(R.id.agnNombre)
        agnAPaterno = findViewById(R.id.agnAPaterno)
        agnAMaterno = findViewById(R.id.agnAMaterno)
        edtFechaCita = findViewById(R.id.edtFechaCita)
        spnHora = findViewById(R.id.spnHora) // Mapeo del Spinner
        btnGuardarCita = findViewById(R.id.btnGuardarCita)
        agnDetalles = findViewById(R.id.agnDetalles)
        btnDictarDetalles = findViewById(R.id.btnDictarDetalles)

        txtTituloServicio = findViewById(R.id.txtConInfo3)
        btnConInicio4 = findViewById(R.id.btnConInicio4)
        btnConPerfil4 = findViewById(R.id.btnConPerfil4)
        btnConConsulta4 = findViewById(R.id.btnConConsulta4)

        // Inicializar Presenter
        val agendaModel = agendaModel()
        presenter = AgendaPresenter(agendaModel)
        presenter.attachView(this)

        // --- LÓGICA DE FORMATO DE FECHA ---
        edtFechaCita.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val cal = Calendar.getInstance()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d]".toRegex(), "")
                    val cleanC = current.replace("[^\\d]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl

                    var index = 2
                    while (index <= cl && index < 6) {
                        index += 2
                    }
                    if (clean == cleanC) sel--

                    if (clean.length < 8) {
                        var ddmmyyyy = clean
                        if (clean.length >= 2) {
                            ddmmyyyy = clean.substring(0, 2) + "/" + clean.substring(2)
                        }
                        if (clean.length >= 4) {
                            ddmmyyyy = ddmmyyyy.substring(0, 5) + "/" + clean.substring(4)
                        }
                        clean = ddmmyyyy
                    } else {
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()

                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                        clean = String.format("%02d/%02d/%02d", day, mon, year)

                        // *** NUEVO: CUANDO LA FECHA ESTÁ COMPLETA, CARGAR HORAS ***
                        presenter.loadAvailableHours(clean)
                    }

                    current = clean
                    edtFechaCita.setText(current)
                    val pos = if (sel < 0) 0 else if (sel < current.length) sel else current.length
                    edtFechaCita.setSelection(if (pos < current.length) current.length else pos)
                }
            }
        })

        // Configurar UI
        if (servicioTitulo != null) {
            txtTituloServicio.text = "Agendando: $servicioTitulo"
        } else {
            txtTituloServicio.text = "Ingresa los siguientes datos"
        }

        // Listeners
        btnGuardarCita.setOnClickListener {
            presenter.handleGuardarCitaClick()
        }

        btnDictarDetalles.setOnClickListener {
            checkMicrophonePermission()
        }

        btnConInicio4.setOnClickListener { navigateToMainActivity() }
        btnConPerfil4.setOnClickListener { navigateToServicesActivity() }
        btnConConsulta4.setOnClickListener { validateAndNavigateToHistorial() }
    }

    // --- NUEVO: IMPLEMENTACIÓN PARA MOSTRAR HORAS ---
    override fun showAvailableHours(horas: List<String>) {
        // Creamos un adaptador personalizado "al vuelo" sin crear archivos XML
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, horas) {

            // 1. Esto controla cómo se ve el texto cuando el Spinner está cerrado (seleccionado)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // Forzamos texto NEGRO
                return view
            }

            // 2. Esto controla cómo se ve la lista cuando la despliegas
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.BLACK) // Texto NEGRO
                view.setBackgroundColor(Color.WHITE) // Fondo BLANCO para asegurar contraste
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnHora.adapter = adapter
    }

    override fun getServicioTitulo(): String = servicioTitulo ?: ""

    override fun showLoading() {
        // Opcional: Podrías mostrar un ProgressBar aquí
    }

    override fun hideLoading() {
        // Ocultar ProgressBar
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun clearForm() {
        agnNombre.setText("")
        agnAPaterno.setText("")
        agnAMaterno.setText("")
        edtFechaCita.setText("")
        // spnHora no necesita limpiarse, se actualizará con la nueva fecha
        agnDetalles.setText("")
        spnHora.adapter = null // Limpiar spinner
    }

    override fun navigateBackToServices() {
        val intent = Intent(this, Servicios::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    override fun getDatosAgendamiento(): Map<String, String> {
        // Obtenemos el valor seleccionado del Spinner.
        // Si es nulo (por ejemplo, si no ha cargado), enviamos una cadena vacía.
        val horaSeleccionada = spnHora.selectedItem?.toString() ?: ""

        return mapOf(
            "nombreCitado" to agnNombre.text.toString(),
            "aPaterno" to agnAPaterno.text.toString(),
            "aMaterno" to agnAMaterno.text.toString(),
            "fechaCita" to edtFechaCita.text.toString(),
            "horaCita" to horaSeleccionada, // Aquí enviamos la hora del Spinner
            "detalles" to agnDetalles.text.toString()
        )
    }

    private fun validateAndNavigateToHistorial() {
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, Historial::class.java))
        } else {
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun checkMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceInput()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                showToast("Necesitamos el permiso del micrófono para grabar tu problema.")
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Describe el problema de tu equipo...")
        }
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            showToast("Tu dispositivo no soporta el dictado de voz.")
        }
    }

    @Deprecated("Usar Activity Result API si es posible, pero forzar para compatibilidad")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results: ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                val spokenText = results[0]
                agnDetalles.setText(spokenText)
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
        finish()
    }
    private fun navigateToServicesActivity() {
        startActivity(Intent(this, Servicios::class.java))
    }
}