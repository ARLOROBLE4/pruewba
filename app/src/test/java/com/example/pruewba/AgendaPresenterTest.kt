package com.example.pruewba.Presentador

import com.example.pruewba.Modelo.agendaModel
import com.example.pruewba.Presentador.Contratos.AgendaContract
import io.mockk.*
import org.junit.Before
import org.junit.Test

/**
 * PRUEBAS UNITARIAS PARA EL FORMULARIO DE AGENDA realizadas por Gabriel Gámez v2.0 (debido a que cambió la versión de la aplicación)
 * Pruebas realizadas:
 * 1.- Validación de datos faltantes: En esta prueba simulamos un intento de envío de campos vacíos
 * el sistema deberá responder un mensaje al usuario indicandole que le hacen falta ingresar datos
 * por ende este mismo no debe llamar al modelo.
 * 2.- "Happy Path" : En esta prueba se simula un intento de envío de campos llenos con su respectiva
 * información al mokcs (simulador de servidor) respondiendo "CORRECTO" y el sistema debe guardar con exito
 * y debe llamar al modelo e inicializar el presentador y la vista
 * 3.- Manejo de error en el servidor: En esta prueba se simula un error en el servidor (un fallo de conexión por ejemplo)
 * respondiendo "ERROR(FALSE)" y el sistema debe mostrar el mensaje adecuado y el formulario no debe borrar
 * los datos ingresados.
 * 4.- Validación de falta de hora: En esta prueba simulamos un intento de envío de datos vacíos (Hora)
 * ejecutando el clic "Guardar cita" y el sistema debe mostrar el mensaje adecuado.
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * ////////////////// Prueba realizada: 03/12/2025 11:59 P.M. /////////////////////////////////////////////////////////////////////////////
 * ///////////////// Resultado: CORRECTO - 4/4 tests passed  ////////////////////////////////////////////////////////////////////////////
 * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

class AgendaPresenterTest {

    private val viewMock = mockk<AgendaContract.View>(relaxed = true)
    private val modelMock = mockk<agendaModel>(relaxed = true)

    private lateinit var presenter: AgendaPresenter

    @Before
    fun setup() {
        presenter = AgendaPresenter(modelMock)
        presenter.attachView(viewMock)
        println("SETUP: Presentador inicializado y vista adjuntada.")
    }

    // --- PRUEBA 1: VALIDACIÓN DE DATOS OBLIGATORIOS ---
    @Test
    fun `testValidacionFaltanDatos`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Validacion de datos faltantes (Nombre vacio)")

        // Escenario
        val datosIncompletos = mapOf(
            "nombreCitado" to "", // VACÍO
            "fechaCita" to "01/01/2025",
            "horaCita" to "10:00"
        )
        every { viewMock.getDatosAgendamiento() } returns datosIncompletos
        println("PASO 1: Datos simulados cargados (Nombre vacio).")

        // Acción
        println("PASO 2: Ejecutando click en guardar cita...")
        presenter.handleGuardarCitaClick()

        // Verificación
        println("PASO 3: Verificando resultados...")
        verify { viewMock.showToast("Faltan datos (Nombre o Fecha).") }
        println("   -> CONFIRMADO: Se mostro el mensaje 'Faltan datos'.")

        verify(exactly = 0) { modelMock.guardarCita(any(), any(), any(), any(), any(), any(), any()) }
        println("   -> CONFIRMADO: El modelo NO fue llamado.")
        println("FIN TEST: Validacion de datos faltantes exitosa.")
    }

    // --- PRUEBA 2: VALIDACIÓN DE HORA ---
    @Test
    fun `testValidacionFaltaHora`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Validacion de falta de hora")

        // Escenario
        val datosSinHora = mapOf(
            "nombreCitado" to "Cliente Prueba",
            "fechaCita" to "01/01/2025",
            "horaCita" to "" // VACÍO
        )
        every { viewMock.getDatosAgendamiento() } returns datosSinHora
        println("PASO 1: Datos simulados cargados (Hora vacia).")

        // Acción
        println("PASO 2: Ejecutando click en guardar cita...")
        presenter.handleGuardarCitaClick()

        // Verificación
        verify { viewMock.showToast("Por favor selecciona un horario disponible.") }
        println("   -> CONFIRMADO: Se mostro el mensaje de seleccionar horario.")

        verify(exactly = 0) { modelMock.guardarCita(any(), any(), any(), any(), any(), any(), any()) }
        println("FIN TEST: Validacion de hora exitosa.")
    }

    // --- PRUEBA 3: GUARDADO EXITOSO ---
    @Test
    fun `testGuardarCitaExitoso`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Guardado de cita exitoso (Happy Path)")

        // Escenario
        val datosCompletos = mapOf(
            "nombreCitado" to "Juan Perez",
            "aPaterno" to "Lopez",
            "aMaterno" to "Gomez",
            "fechaCita" to "01/01/2025",
            "horaCita" to "10:00",
            "detalles" to "Formateo"
        )
        every { viewMock.getDatosAgendamiento() } returns datosCompletos

        // Simulación del Servidor
        every {
            modelMock.guardarCita(any(), any(), any(), any(), any(), any(), captureLambda())
        } answers {
            println("   -> MOCK: El servidor falso ha recibido la solicitud.")
            val callback = lastArg<(Boolean, String) -> Unit>()
            println("   -> MOCK: Respondiendo EXITO (true).")
            callback.invoke(true, "Cita agendada correctamente")
        }

        // Acción
        println("PASO 1: Ejecutando click en guardar cita...")
        presenter.handleGuardarCitaClick()

        // Verificaciones
        println("PASO 2: Verificando comportamiento de la UI...")

        verify { viewMock.showLoading() }
        println("   -> CONFIRMADO: Se mostro Loading.")

        verify { viewMock.hideLoading() }
        println("   -> CONFIRMADO: Se oculto Loading.")

        verify { viewMock.showToast("Cita agendada correctamente") }
        println("   -> CONFIRMADO: Se mostro Toast de exito.")

        verify { viewMock.clearForm() }
        println("   -> CONFIRMADO: Se limpio el formulario.")

        verify { viewMock.navigateBackToServices() }
        println("   -> CONFIRMADO: Se navego hacia atras.")

        println("FIN TEST: Guardado exitoso completado.")
    }

    // --- PRUEBA 4: ERROR AL GUARDAR ---
    @Test
    fun `testGuardarCitaErrorServidor`() {
        println("--------------------------------------------------")
        println("INICIO TEST: Manejo de error del servidor")

        val datosCompletos = mapOf(
            "nombreCitado" to "Juan Perez",
            "fechaCita" to "01/01/2025",
            "horaCita" to "10:00"
        )
        every { viewMock.getDatosAgendamiento() } returns datosCompletos

        // Simulación de Error
        every {
            modelMock.guardarCita(any(), any(), any(), any(), any(), any(), captureLambda())
        } answers {
            println("   -> MOCK: El servidor falso ha recibido la solicitud.")
            val callback = lastArg<(Boolean, String) -> Unit>()
            println("   -> MOCK: Respondiendo ERROR (false) - Horario ocupado.")
            callback.invoke(false, "Horario ya ocupado")
        }

        // Acción
        println("PASO 1: Ejecutando click en guardar cita...")
        presenter.handleGuardarCitaClick()

        // Verificaciones
        verify { viewMock.showToast("Horario ya ocupado") }
        println("   -> CONFIRMADO: Se mostro el mensaje de error del servidor.")

        verify(exactly = 0) { viewMock.clearForm() }
        println("   -> CONFIRMADO: El formulario NO se borro (Correcto).")

        println("FIN TEST: Manejo de error completado.")
    }
}