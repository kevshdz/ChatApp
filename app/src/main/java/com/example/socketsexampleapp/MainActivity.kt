package com.example.socketsexampleapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.socketsexampleapp.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException



class MainActivity : AppCompatActivity() {
    private lateinit var mSocket: Socket
    private lateinit var _binding :ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        try {
            mSocket = IO.socket("http://10.0.2.2:5001")
            //mSocket = IO.socket("https://da-pw.mx:3000")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("message", onMessageReceived)
        //mSocket.on("geocoding", onData)
        mSocket.connect()
    }

    private val onConnect = Emitter.Listener {
        runOnUiThread {
            // Conectado al servidor
            mSocket.emit("message", "Conectamos a pinche Socket")
            mSocket.emit("connected", "Kevin")
            //mSocket.emit("message", "Conectamos a pinche Socket")
            //mSocket.emit("geocoding", "Data")
            //sendGeoData()
        }
    }

    private val onMessageReceived = Emitter.Listener { args ->
        runOnUiThread {
            val data = args[0] as String
            // Manejar el mensaje recibido
            _binding.textShow.setText(data)
            println("Message received: $data")
        }
    }

    private val onData = Emitter.Listener { args ->
        runOnUiThread {
            Log.e("Data", args[0].toString())
        }

    }

    private fun sendGeoData() {
        val geoData = GeoData(
            id = 12345,
            latitude = 37.7749,
            longitude = -122.4194,
            status = "active",
            zone = "example_zone"
        )

        mSocket.emit("geocoding", geoData.id, geoData.latitude, geoData.longitude, geoData.status, geoData.zone)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off("message", onMessageReceived)
    }
    }

