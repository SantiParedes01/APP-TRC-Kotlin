package com.example.tigrerugbyclub

import org.json.JSONArray

data class Jugador(
    val id: String,
    val nombre: String,
    val posicion: String,
    val numero: String
)

fun parseJugadores(responseData: String): List<Jugador> {
    val jsonArray = JSONArray(responseData)
    val jugadores = mutableListOf<Jugador>()
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val id = jsonObject.getString("id")
        val nombre = jsonObject.getString("name")
        val posicion = jsonObject.getString("position")
        val numero = jsonObject.getString("number")
        jugadores.add(Jugador(id, nombre, posicion, numero))
    }
    return jugadores
}