package com.example.tigrerugbyclub

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

fun addJugador(nombre: String, posicion: String, numero: String, onDone: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()
        val json = """
            {
                "name": "$nombre",
                "position": "$posicion",
                "number": "$numero"
            }
        """.trimIndent()
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://6ab28d805b9b60f39d34de6a.mockapi.io/jugadores")
            .post(requestBody)
            .build()
        val response = client.newCall(request).execute()
        Log.d("MiApp", "Alta: ${response.body?.string()}")
        launch(Dispatchers.Main) { onDone() }
    }
}

fun updateJugador(id: String, nombre: String, posicion: String, numero: String, onDone: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()
        val json = """
            {
                "name": "$nombre",
                "position": "$posicion",
                "number": "$numero"
            }
        """.trimIndent()
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://6ab28d805b9b60f39d34de6a.mockapi.io/jugadores/$id")
            .put(requestBody)
            .build()
        val response = client.newCall(request).execute()
        Log.d("MiApp", "Update: ${response.body?.string()}")
        launch(Dispatchers.Main) { onDone() }
    }
}

fun deleteJugador(id: String, onResult: (Boolean) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://6ab28d805b9b60f39d34de6a.mockapi.io/jugadores/$id")
            .delete()
            .build()
        val response = client.newCall(request).execute()
        launch(Dispatchers.Main) {
            onResult(response.isSuccessful)
        }
    }
}

fun fetchJugadores(onResult: (List<Jugador>) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://6ab28d805b9b60f39d34de6a.mockapi.io/jugadores")
            .build()
        val response = client.newCall(request).execute()
        val responseData = response.body?.string()
        launch(Dispatchers.Main) {
            Log.d("MiApp", "Respuesta: $responseData")
            if (responseData != null) {
                onResult(parseJugadores(responseData))
            }
        }
    }
}