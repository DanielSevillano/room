package com.daniel.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun Dialogo(
    nuevaPersona: (persona: Persona) -> Unit,
    cerrarDialogo: () -> Unit
) {
    var textoNombre by rememberSaveable { mutableStateOf("") }
    var textoApellido by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = cerrarDialogo,
        confirmButton = {
            TextButton(
                onClick = {
                    val persona = Persona(
                        nombre = textoNombre,
                        apellido = textoApellido
                    )

                    nuevaPersona(persona)
                    cerrarDialogo()
                },
                enabled = textoNombre.isNotEmpty() && textoApellido.isNotEmpty()
            ) {
                Text(text = "Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = cerrarDialogo) {
                Text(text = "Cancelar")
            }
        },
        title = { Text(text = "Nueva persona") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = textoNombre,
                    onValueChange = { textoNombre = it },
                    label = { Text(text = "Nombre") }
                )

                TextField(
                    value = textoApellido,
                    onValueChange = { textoApellido = it },
                    label = { Text(text = "Apellido") }
                )
            }
        }
    )
}