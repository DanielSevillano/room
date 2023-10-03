package com.daniel.room

import androidx.room.Entity

@Entity(primaryKeys = ["nombre", "apellido"])
data class Persona(
    val nombre: String,
    val apellido: String
)