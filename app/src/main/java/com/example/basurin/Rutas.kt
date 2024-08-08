package com.example.basurin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Rutas : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutas)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val icon1: ImageButton = findViewById(R.id.camionRuta)
        val icon2: ImageButton = findViewById(R.id.notificar)
        val icon3: ImageButton = findViewById(R.id.home)
        val icon4: ImageButton = findViewById(R.id.educar)
        val icon5: ImageButton = findViewById(R.id.reportar)

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

        createFragment()
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initializeFirebaseDatabase() {
        database = FirebaseDatabase.getInstance().getReference("gps_data")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map.clear()  // Clear existing markers
                val latitude = dataSnapshot.child("latitude").getValue(Double::class.java)
                val longitude = dataSnapshot.child("longitude").getValue(Double::class.java)

                // Añade logs para depurar
                Log.d("FirebaseData", "Latitud: $latitude, Longitud: $longitude")

                if (latitude != null && longitude != null) {
                    val coordinates = LatLng(latitude, longitude)
                    val marker = MarkerOptions().position(coordinates).title("Ubicación")
                    map.addMarker(marker)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f))
                } else {
                    Log.e("FirebaseData", "Latitud o Longitud es nulo")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RutasActivity", "Failed to read value.", error.toException())
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initializeFirebaseDatabase()
    }
}
