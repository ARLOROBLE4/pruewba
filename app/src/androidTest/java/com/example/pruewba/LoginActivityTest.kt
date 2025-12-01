package com.example.pruewba.Vistas

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pruewba.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Login::class.java)

    @Test
    fun verificarElementosVisiblesAlInicio() {
        onView(withId(R.id.edtLoginEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.edtLoginPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLoguear)).check(matches(isDisplayed()))
    }

    @Test
    fun flujoDeEscrituraEnCampos() {
        val emailPrueba = "usuario@prueba.com"
        val passPrueba = "password123"

        // Escribir email
        onView(withId(R.id.edtLoginEmail))
            .perform(typeText(emailPrueba), closeSoftKeyboard())
            .check(matches(withText(emailPrueba)))

        // Escribir contraseña
        onView(withId(R.id.edtLoginPassword))
            .perform(typeText(passPrueba), closeSoftKeyboard())
            .check(matches(withText(passPrueba))) // Nota: Verifica el texto interno, aunque visualmente sea asteriscos

        // Click en botón (No validamos navegación aquí para evitar llamadas reales a red en UI Test básico)
        onView(withId(R.id.btnLoguear)).perform(click())
    }
}