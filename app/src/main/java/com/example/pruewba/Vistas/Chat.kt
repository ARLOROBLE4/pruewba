package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruewba.R
// Puedes importar tu Presenter/Contract aquí si usas MVP completo para el chat.
// import com.example.pruewba.Presentador.Contratos.ChatContract

class Chat : AppCompatActivity() { // Si usas MVP, debe ser: Chat : AppCompatActivity(), ChatContract.View

    // Vistas mapeadas de activity_chat.xml
    private lateinit var rcvChat: RecyclerView
    private lateinit var edtChat: EditText
    private lateinit var btnEnviar: Button

    // Botones de navegación (para completar la UI)
    private lateinit var btnConInicio: Button
    private lateinit var btnConPerfil: Button
    private lateinit var btnConConsulta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        // El ConstraintLayout principal debe tener un ID si quieres usar esto
        // Aquí asumimos que el ConstraintLayout principal de activity_chat.xml no tiene ID "main"
        // Si tu ConstraintLayout principal tuviera el ID: android:id="@+id/main" en activity_chat.xml,
        // podrías usar ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {...}

        // 1. Mapeo de Vistas
        rcvChat = findViewById(R.id.rcvChat)
        edtChat = findViewById(R.id.edtChat)
        btnEnviar = findViewById(R.id.button) // ID del botón "Enviar"

        // Mapeo de botones de navegación (aunque no tendrán lógica de chat)
        btnConInicio = findViewById(R.id.btnConInicio2)
        btnConPerfil = findViewById(R.id.btnConPerfil2)
        btnConConsulta = findViewById(R.id.btnConConsulta2)


        // 2. Configurar RecyclerView para el Chat
        // Usa LinearLayoutManager para que los ítems se muestren verticalmente
        rcvChat.layoutManager = LinearLayoutManager(this).apply {
            // Opcional: Esto ayuda a que el RecyclerView muestre los mensajes desde abajo
            stackFromEnd = true
        }

        // Nota: Aquí se debería asignar un adaptador (ChatAdapter) para mostrar los mensajes.

        // 3. Configurar Listener para el botón Enviar
        btnEnviar.setOnClickListener {
            enviarMensaje()
        }

        // 4. Configurar Listeners de Navegación (Ejemplo: Volver a inicio)
        btnConInicio.setOnClickListener {
            // Ejemplo de navegación de vuelta a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Lógica para enviar el mensaje.
     * Aquí iría la llamada al Presenter o ViewModel para guardar y enviar el mensaje.
     */
    private fun enviarMensaje() {
        val mensaje = edtChat.text.toString().trim()

        if (mensaje.isNotEmpty()) {
            // 1. Aquí se llamaría a presenter.sendMessage(mensaje)

            // 2. Limpiar el EditText
            edtChat.setText("")

            // 3. (Opcional) Mostrar una confirmación simple (temporal)
            Toast.makeText(this, "Mensaje enviado: $mensaje", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Escribe un mensaje antes de enviar.", Toast.LENGTH_SHORT).show()
        }
    }
}