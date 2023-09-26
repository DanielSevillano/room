package com.daniel.room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import com.daniel.room.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PersonaDatabase::class.java,
            "personas.db"
        ).build()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var dialogoAbierto by rememberSaveable { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()
                    val flujo = db.dao.obtenerPersonas()
                    var personas: List<Persona> by remember {
                        mutableStateOf(emptyList())
                    }
                    
                    LaunchedEffect(key1 = true) {
                        scope.launch {
                            flujo.collect { lista ->
                                personas = lista
                            }
                        }
                    }

                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(text = "Personas")
                                },
                                actions = {
                                    IconButton(onClick = {
                                        scope.launch { 
                                            db.dao.eliminarTodos()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Eliminar todos"
                                        )
                                    }
                                }
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(onClick = { dialogoAbierto = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Añadir"
                                )
                            }
                        }
                    ) { paddingValues ->
                        LazyColumn(
                            modifier = Modifier.padding(paddingValues),
                            contentPadding = PaddingValues(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(personas) { persona ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${persona.nombre} ${persona.apellido}",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    IconButton(onClick = {
                                        scope.launch {
                                            db.dao.eliminarPersona(persona)
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Clear,
                                            contentDescription = "Eliminar"
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (dialogoAbierto) {
                        Dialogo(
                            nuevaPersona = { persona ->
                                scope.launch {
                                    db.dao.insertarPersona(persona)
                                }
                            },
                            cerrarDialogo = { dialogoAbierto = false }
                        )
                    }
                }
            }
        }
    }
}