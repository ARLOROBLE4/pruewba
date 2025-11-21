package com.example.pruewba.Vistas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruewba.Modelo.registroContract
import com.example.pruewba.Presentador.registroPresenter
import com.example.pruewba.R
import com.example.pruewba.Modelo.accessModel

class registro: AppCompatActivity(), registroContract.View {
    private lateinit var etNombre: EditText
    private lateinit var etAPaterno: EditText
    private lateinit var etAMaterno: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var presenter: registroContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro) // Usando tu layout activity_registro.xml

        // 1. Enlazar Views con los IDs de activity_registro.xml
        etNombre = findViewById(R.id.edtRegistroNombre)
        etAPaterno = findViewById(R.id.edtRegistroAPaterno)
        etAMaterno = findViewById(R.id.edtRegistroAMaterno)
        etEmail = findViewById(R.id.edtRegistroEmail)
        etPassword = findViewById(R.id.edtRegistroPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        // 2. Inicializar Presenter
        presenter = registroPresenter(accessModel())
        presenter.attachView(this)
        // 3. Manejar Click del botón Registrar
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val aPaterno = etAPaterno.text.toString()
            val aMaterno = etAMaterno.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            presenter.handleRegistrationButtonClick(nombre, aPaterno, aMaterno, email, password)
        }
    }

    override fun showRegistrationSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showRegistrationError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun closeScreen() {
        finish() // Cierra la pantalla de registro
    }
}