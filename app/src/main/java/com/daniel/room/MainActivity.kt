package com.daniel.room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import com.daniel.room.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            PersonaDatabase::class.java,
            "personas"
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
                    val dao = database.dao()
                    val flujo = dao.obtenerPersonas()
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
                                            dao.eliminarTodos()
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
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 300.dp),
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(paddingValues),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(personas) { persona ->
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = "${persona.nombre} ${persona.apellido}",
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    },
                                    trailingContent = {
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    dao.eliminarPersona(persona)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Clear,
                                                contentDescription = "Eliminar"
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    if (dialogoAbierto) {
                        Dialogo(
                            nuevaPersona = { persona ->
                                scope.launch {
                                    dao.insertarPersona(persona)
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