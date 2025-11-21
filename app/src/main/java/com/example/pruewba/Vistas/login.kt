package com.example.pruewba.Vistas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruewba.Modelo.accessModel
import com.example.pruewba.Modelo.loginContract
import com.example.pruewba.Presentador.loginPresenter
import com.example.pruewba.R

class login : AppCompatActivity(), loginContract.View {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAcceder: Button
    private lateinit var presenter: loginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.edtLoginEmail)
        etPassword = findViewById(R.id.edtLoginPassword)
        btnAcceder = findViewById(R.id.btnLoguear)

        presenter = loginPresenter(accessModel())
        presenter.attachView(this)

        btnAcceder.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            presenter.handleLoginButtonClick(email, password)
        }
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun navigateToRegistration() {
        //no se ocupa
    }

    override fun navigateToMainScreen() {
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finish()

    }
}