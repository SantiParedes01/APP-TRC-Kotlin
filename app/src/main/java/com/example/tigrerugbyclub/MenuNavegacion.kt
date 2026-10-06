package com.example.tigrerugbyclub

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPrincipal() {
    val navController = rememberNavController()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tigre Rugby Club") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("Acerca de") }, onClick = {
                            expanded = false
                            navController.navigate("acercade")
                        })
                        DropdownMenuItem(text = { Text("Jugadores") }, onClick = {
                            expanded = false
                            navController.navigate("jugadores")
                        })
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "jugadores", modifier = Modifier.padding(innerPadding)) {
            composable("acercade") { PantallaAcercaDe() }
            composable("jugadores") { PantallaJugadores() }
        }
    }
}

@Composable
fun PantallaAcercaDe() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Integrantes del equipo", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.foto1),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Santiago Paredes")
        }
    }
}

@Composable
fun PantallaJugadores() {
    val jugadores = remember { mutableStateListOf<Jugador>() }
    var nombreInput by remember { mutableStateOf("") }
    var posicionInput by remember { mutableStateOf("") }
    var numeroInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        TextField(value = nombreInput, onValueChange = { nombreInput = it }, label = { Text("Nombre") })
        TextField(value = posicionInput, onValueChange = { posicionInput = it }, label = { Text("Posición") })
        TextField(value = numeroInput, onValueChange = { numeroInput = it }, label = { Text("Número") })

        Button(onClick = {
            addJugador(nombreInput, posicionInput, numeroInput) {
                fetchJugadores { lista -> jugadores.clear(); jugadores.addAll(lista) }
            }
            nombreInput = ""
            posicionInput = ""
            numeroInput = ""
        },
            colors = buttonColors(containerColor = Color(0xFF084A81))
        ) {
            Text("Agregar jugador")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            fetchJugadores { lista ->
                jugadores.clear()
                jugadores.addAll(lista)
            } },
            colors = buttonColors(containerColor = Color(0xFF084A81))
        ) {
            Text("Cargar jugadores")
        }

        Spacer(modifier = Modifier.height(16.dp))

        var editingId by remember { mutableStateOf("") }
        var editNombre by remember { mutableStateOf("") }
        var editPosicion by remember { mutableStateOf("") }
        var editNumero by remember { mutableStateOf("") }

        LazyColumn {
            itemsIndexed(jugadores) { _, jugador ->
                if (jugador.id == editingId) {
                    Column {
                        TextField(value = editNombre, onValueChange = { editNombre = it }, label = { Text("Nombre") })
                        TextField(value = editPosicion, onValueChange = { editPosicion = it }, label = { Text("Posición") })
                        TextField(value = editNumero, onValueChange = { editNumero = it }, label = { Text("Número") })
                        Button(onClick = {
                            updateJugador(jugador.id, editNombre, editPosicion, editNumero) {
                                fetchJugadores { lista -> jugadores.clear(); jugadores.addAll(lista) }
                            }
                            editingId = ""
                        }) {
                            Text("Guardar")
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${jugador.nombre} - ${jugador.posicion} - #${jugador.numero}", modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            editingId = jugador.id
                            editNombre = jugador.nombre
                            editPosicion = jugador.posicion
                            editNumero = jugador.numero
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            deleteJugador(jugador.id) { success ->
                                if (success) jugadores.remove(jugador)
                            }
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                        }
                    }
                }
            }
        }
    }
}