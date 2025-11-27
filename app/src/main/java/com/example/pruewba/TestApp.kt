package com.example.pruewba.Pruebas

import android.util.Log
import java.util.Random

/**
 * Objeto de Pruebas de Sistema (Integration Testing).
 * Ejecuta flujos completos usando la red real.
 * TAG para Logcat: "QA_STATUS"
 */
object TestApp {

    private val TAG = "QA_STATUS"
    private val model = accessModel() // Instancia real del modelo con Retrofit

    // Función maestra que ejecuta todas las pruebas en cadena
    fun ejecutarTodasLasPruebas() {
        Log.i(TAG, "===== INICIANDO PRUEBAS DEL SISTEMA =====")

        // 1. Prueba de Login Fallido (Intencional)
        testLoginFallido()

        // 2. Prueba de Login Exitoso
        testLoginExitoso()

        // 3. Prueba de Registro
        testRegistroNuevo()
    }

    private fun testLoginExitoso() {
        // DATOS DE PRUEBA:
        val emailReal = "vero@example.com"
        val passReal = "vero123"

        Log.d(TAG, "--> Ejecutando Test: Login Exitoso...")

        model.iniciarSesion(emailReal, passReal) { exito, mensaje ->
            if (exito) {
                Log.i(TAG, " |||| CORRECTO |||||  Login Exitoso: El servidor respondió correctamente.")
                Log.d(TAG, "   Detalle: $mensaje")
            } else {
                Log.e(TAG, " |||| FALLÓ ||||| Login Exitoso: Se esperaba éxito pero falló.")
                Log.e(TAG, "   Error: $mensaje")
            }
        }
    }

    private fun testLoginFallido() {
        val emailFalso = "usuario_no_existe@mail.com"
        val passFalso = "999999"

        Log.d(TAG, "--> Ejecutando Test: Login Fallido (Intencional)...")

        model.iniciarSesion(emailFalso, passFalso) { exito, mensaje ->
            if (!exito) {
                Log.i(TAG, "|||| CORRECTO |||| Login Fallido: El sistema detectó correctamente las credenciales erróneas.")
                Log.d(TAG, "   Mensaje del server: $mensaje")
            } else {
                Log.e(TAG, "|||| FALLÓ |||| Login Fallido: El sistema dejó entrar a un usuario falso.")
            }
        }
    }

    private fun testRegistroNuevo() {
        // Generamos datos aleatorios para que no diga "El usuario ya existe"
        val randomId = Random().nextInt(1000)
        val nuevoUsuario = "Tester$randomId"
        val nuevoEmail = "qa_test_$randomId@test.com"

        Log.d(TAG, "--> Ejecutando Test: Registro de Usuario ($nuevoUsuario)...")

        model.registrarUsuario(
            nombreUsuario = nuevoUsuario,
            apellidoPaterno = "ApellidoTest",
            apellidoMaterno = "MaternoTest",
            email = nuevoEmail,
            password = "password123"
        ) { exito, mensaje ->
            if (exito) {
                Log.i(TAG, " |||| Correcto |||| Registro: Usuario creado correctamente en la BD.")
                Log.d(TAG, "   Respuesta: $mensaje")
            } else {
                Log.e(TAG, " |||| Error |||| Registro: No se pudo crear el usuario.")
                Log.e(TAG, "   Error: $mensaje")
            }
        }
    }
}