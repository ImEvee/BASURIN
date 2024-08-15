package com.example.basurin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Avisos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avisos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val icon1: ImageButton = findViewById(R.id.btnRutas)
        val icon2: ImageButton = findViewById(R.id.btnNotificacion)
        val icon3: ImageButton = findViewById(R.id.btnInicio)
        val icon4: ImageButton = findViewById(R.id.btnEduca)
        val icon5: ImageButton = findViewById(R.id.btnReportes)

        icon1.setOnClickListener {
            startActivity(Intent(this, Rutas::class.java))
        }

        icon2.setOnClickListener {
            startActivity(Intent(this, Avisos::class.java))
        }

        icon3.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        icon4.setOnClickListener {
            startActivity(Intent(this, Educa::class.java))
        }

        icon5.setOnClickListener {
            startActivity(Intent(this, Reportes::class.java))
        }

        // Crear un canal de notificación
        createNotificationChannel()

        // Obtener referencia de la base de datos
        var database = FirebaseDatabase.getInstance().reference

        // Referencia al TextView donde se mostrará el aviso
        val avisoTextView: TextView = findViewById(R.id.textView14)

        // Leer los datos del nodo "avisos"
        database.child("avisos").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val aviso = snapshot.getValue(String::class.java)
                avisoTextView.text = aviso ?: "No hay avisos disponibles"

                // Enviar notificación cuando se reciba un nuevo aviso
                aviso?.let { sendNotification(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de errores en caso de que la consulta sea cancelada
            }
        })
    }

    private fun createNotificationChannel() {
        // Crear un canal de notificación para API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "avisos_channel"
            val name = "Canal de Avisos"
            val descriptionText = "Canal para las notificaciones de nuevos avisos"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Registrar el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(aviso: String) {
        val channelId = "avisos_channel"
        val intent = Intent(this, Avisos::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notificacion) // Icono de la notificación (debe estar en tu carpeta drawable)
            .setContentTitle("Nuevo Aviso")
            .setContentText(aviso)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // Enviar la notificación con un ID único
            notify(0, builder.build())
        }
    }
}

