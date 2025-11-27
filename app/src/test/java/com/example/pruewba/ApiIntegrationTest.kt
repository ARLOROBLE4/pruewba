package com.example.pruewba.Pruebas

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

/**
 * Pruebas de Integración y Rendimiento (JVM).
 * NO requiere emulador. Se ejecuta directamente en PC.
 */
class ApiIntegrationTest {

    // Instancia real del modelo (se conectará a URL configurada en accessModel)
    private val model = accessModel()

    /**
     * PRUEBA DE RENDIMIENTO (Performance Test)
     * Mide cuánto tarda el servidor en responder a un Login.
     * Útil para detectar lentitud en la API o la red.
     */
    @Test
    fun `testTiempoRespuestaLogin_DebeSerMenorA2Segundos`() {
        // 1. Preparación
        val email = "vero@example.com"
        val password = "vero123"
        val maxTimeMs = 2000L

        // CountDownLatch sirve para "pausar" el test hasta tener respuesta
        val lock = CountDownLatch(1)
        var loginExitoso = false

        println("Iniciando prueba de latencia...")

        // 2. Ejecución y Medición
        val tiempoTotal = measureTimeMillis {
            model.iniciarSesion(email, password) { success, message ->
                loginExitoso = success
                println("   Respuesta recibida: $message")
                lock.countDown() // liberamos el test
            }

            // Esperamos hasta 5 segundos a que responda el servidor
            lock.await(5, TimeUnit.SECONDS)
        }

        println("Tiempo de respuesta: ${tiempoTotal}ms")

        // 3. Verificaciones (Asserts)
        assertTrue("El login debería ser exitoso para medir tiempos reales", loginExitoso)

        if (tiempoTotal > maxTimeMs) {
            System.err.println("|||| ADVERTENCIA ||||: La API es lenta (${tiempoTotal}ms).")
        } else {
            println("Rendimiento óptimo.")
        }

        // Fallar el test si supera el límite estricto
        assertTrue("El tiempo de respuesta fue muy alto: ${tiempoTotal}ms", tiempoTotal < maxTimeMs)
    }

    /**
     * PRUEBA DE LÓGICA: Usuario Duplicado
     * Intenta registrar un usuario que ya existe para ver si la API lo bloquea.
     */
    @Test
    fun `testRegistroDuplicado_DebeRetornarError`() {
        println("Iniciando prueba de usuario duplicado...")

        // 1. Preparación: Datos de un usuario que YA EXISTE
        val emailExistente = "vero@example.com"
        val pass = "vero123"

        val lock = CountDownLatch(1)
        var registroExitoso = true // Asumimos true para que falle si no cambia
        var mensajeRespuesta = ""

        // 2. Ejecución
        model.registrarUsuario(
            "Usuario", "Test", "Duplicado",
            emailExistente, pass
        ) { success, message ->
            registroExitoso = success
            mensajeRespuesta = message
            lock.countDown()
        }

        lock.await(5, TimeUnit.SECONDS)

        // 3. Verificación
        println("   Resultado servidor: $mensajeRespuesta")

        // Esperamos que sea FALSE (que NO haya permitido el registro)
        assertFalse("El sistema permitió registrar un correo duplicado", registroExitoso)

        // Verificamos que el mensaje sea coherente (ajusta el texto según tu PHP)
        // Ejemplo: "El usuario ya existe" o similar.
        // assertTrue(mensajeRespuesta.contains("existe", ignoreCase = true))
    }
}