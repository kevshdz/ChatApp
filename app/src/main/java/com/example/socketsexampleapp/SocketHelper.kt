package com.example.socketsexampleapp

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class SocketHelper(private val url: String) {
}
data class GeoData(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val zone: String
)
