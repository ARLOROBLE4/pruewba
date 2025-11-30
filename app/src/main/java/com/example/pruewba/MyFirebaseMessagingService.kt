package com.example.pruewba

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pruewba.Modelo.FCMModel
import com.example.pruewba.Modelo.SesionManager
import com.example.pruewba.Vistas.Historial
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCMService"

    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo Token: $token")
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Mensaje recibido de: ${remoteMessage.from}")

        // 🛑 LÓGICA DE SESIÓN: Solo procesar si el usuario está logueado
        val sessionManager = SesionManager(applicationContext)
        if (sessionManager.isLoggedIn()) {
            remoteMessage.notification?.let {
                sendNotification(it.title, it.body, remoteMessage.data)
            }
        } else {
            Log.d(TAG, "Notificación ignorada, el usuario no está logueado.")
        }
    }

    // Envía el token al servidor si hay una sesión activa
    private fun sendRegistrationToServer(token: String?) {
        if (token == null) return

        val sessionManager = SesionManager(applicationContext)
        val userId = sessionManager.getUserId()

        if (userId > 0) {
            FCMModel().sendTokenToServer(userId, token) { isSuccess, message ->
                if (isSuccess) {
                    Log.d(TAG, "Token enviado al servidor exitosamente: $message")
                } else {
                    Log.e(TAG, "Fallo al enviar token al servidor: $message")
                }
            }
        }
    }

    // Construye y muestra la notificación
    private fun sendNotification(title: String?, body: String?, data: Map<String, String>) {
        val intent = Intent(this, Historial::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            data["registro_id"]?.let { putExtra("highlight_registro_id", it.toInt()) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logoapp) // 🛑 Asegúrate de tener este drawable
            .setContentTitle(title ?: "PC Status Update")
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Actualizaciones de Estado de Equipo",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}