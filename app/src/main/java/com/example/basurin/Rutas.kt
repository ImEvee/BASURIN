package com.example.basurin

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult


class Rutas : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var requestNotificationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: LatLng? = null
    private var userMarker: Marker? = null
    private var gpsMarker: Marker? = null
    private val polylineList = mutableListOf<Polyline>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutas)
        enableEdgeToEdge()

        // Inicializa el lanzador de permisos de notificaciones
        requestNotificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso de notificaciones concedido, ahora solicita el permiso de localización
                checkLocationPermission()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Inicializa el lanzador de permisos de localización
        requestLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso de localización concedido, inicializa la ubicación
                initializeLocation()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        // Solicita permisos de notificaciones si es Android 13 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        } else {
            // No es necesario solicitar permisos de notificaciones para versiones anteriores a Android 13
            checkLocationPermission()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
    }

    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso de notificaciones ya concedido, solicita el permiso de localización
                checkLocationPermission()
            }
            else -> {
                // Solicita permiso de notificaciones
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso de localización ya concedido, inicializa la ubicación
                initializeLocation()
            }
            else -> {
                // Solicita permiso de localización
                requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun setupUI() {
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

        createFragment()
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun initializeLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        if (!isGpsEnabled()) {
            Toast.makeText(this, "El GPS está apagado. Solo se mostrará la ruta del camión.", Toast.LENGTH_LONG).show()
            initializeFirebaseDatabase()
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
                Log.d("UserLocation", "Ubicación obtenida con lastLocation: $userLocation")
                updateUserMarker(userLocation!!)
            } else {
                Log.e("LocationError", "La ubicación es nula, intentando requestLocationUpdates")
                requestLocationUpdates()
            }
        }.addOnFailureListener { e ->
            Log.e("LocationError", "Error al obtener la última ubicación conocida", e)
            Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 segundos
            fastestInterval = 5000 // 5 segundos
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    userLocation = LatLng(location.latitude, location.longitude)
                    Log.d("UserLocation", "Ubicación obtenida con requestLocationUpdates: $userLocation")
                    updateUserMarker(userLocation!!)
                } ?: run {
                    Log.e("LocationError", "La ubicación es nula en requestLocationUpdates")
                    Toast.makeText(this@Rutas, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }, null)
    }

    private fun updateUserMarker(location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title("Mi Ubicación")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.usericon))

        userMarker?.remove()

        userMarker = map.addMarker(markerOptions)

        if (map.cameraPosition.zoom < 10f) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
        }
    }

    private fun calculateDistance(location1: LatLng, location2: LatLng): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            location1.latitude, location1.longitude,
            location2.latitude, location2.longitude,
            results
        )
        return results[0]
    }

    private fun sendNotification() {
        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager

        // Configurar el canal de notificación para Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "nearby_channel",
                "Camión Cercano",
                NotificationManager.IMPORTANCE_HIGH  // Asegúrate de que la importancia sea alta para notificaciones flotantes
            ).apply {
                description = "Notificación cuando el camión esté cerca"
                enableVibration(true)  // Habilitar vibración
                vibrationPattern = longArrayOf(0, 500, 1000, 500, 1000)  // Patrón de vibración (0ms de espera, 500ms de vibración, 1000ms de pausa, 500ms de vibración, 1000ms de pausa)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Crear la notificación
        val builder = NotificationCompat.Builder(this, "nearby_channel")
            .setSmallIcon(R.drawable.notificacion)  // Reemplaza con tu propio icono
            .setContentTitle("Camión Cercano")
            .setContentText("El camión está cerca de tu ubicación.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Asegúrate de que la prioridad esté configurada para mostrar notificaciones flotantes
            .setDefaults(NotificationCompat.DEFAULT_ALL)  // Usa todas las configuraciones por defecto (incluye vibración)
            .setVibrate(longArrayOf(0, 500, 1000, 500, 1000))  // Patrón de vibración
            .setAutoCancel(true)  // Cierra la notificación cuando el usuario la toca

        // Mostrar la notificación
        notificationManager.notify(1, builder.build())
    }


    private fun initializeFirebaseDatabase() {
        val gpsDataRef = FirebaseDatabase.getInstance().getReference("gps_data")
        val routesRef = FirebaseDatabase.getInstance().getReference("rutas/ruta1")

        gpsDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gpsMarker?.remove()

                val latitude = dataSnapshot.child("latitude").getValue(Double::class.java)
                val longitude = dataSnapshot.child("longitude").getValue(Double::class.java)

                Log.d("FirebaseData", "Latitud GPS: $latitude, Longitud GPS: $longitude")

                if (latitude != null && longitude != null) {
                    val coordinates = LatLng(latitude, longitude)

                    val markerOptions = MarkerOptions()
                        .position(coordinates)
                        .title("Camion")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gpscamion))

                    gpsMarker = map.addMarker(markerOptions)

                    userLocation?.let { userLoc ->
                        val distance = calculateDistance(userLoc, coordinates)
                        Log.d("DistanceCheck", "Distancia entre usuario y camión: $distance metros")

                        if (distance < 800) {
                            sendNotification()
                        }
                    }

                } else {
                    Log.e("FirebaseData", "Latitud o Longitud es nulo en gps_data")
                }

                polylineList.forEach { it.remove() }
                polylineList.clear()

                routesRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(routeSnapshot: DataSnapshot) {
                        val routePoints = mutableListOf<LatLng>()
                        for (snapshot in routeSnapshot.children) {
                            val routeLatitude = snapshot.child("latitude").getValue(Double::class.java)
                            val routeLongitude = snapshot.child("longitude").getValue(Double::class.java)
                            if (routeLatitude != null && routeLongitude != null) {
                                routePoints.add(LatLng(routeLatitude, routeLongitude))
                            } else {
                                Log.e("FirebaseData", "Latitud o Longitud es nulo en uno de los puntos de ruta")
                            }
                        }

                        Log.d("FirebaseData", "Ruta: $routePoints")

                        if (routePoints.isNotEmpty()) {
                            val polylineOptions = PolylineOptions()
                                .addAll(routePoints)
                                .width(15f)
                                .color(Color.RED)
                                .geodesic(true)

                            val polyline = map.addPolyline(polylineOptions)
                            if (polyline != null) {
                                polylineList.add(polyline)
                            }
                        } else {
                            Log.e("FirebaseData", "No se encontraron puntos de ruta")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("RutasActivity", "Failed to read route value.", error.toException())
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("RutasActivity", "Failed to read gps_data value.", error.toException())
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initializeFirebaseDatabase()
    }
}

