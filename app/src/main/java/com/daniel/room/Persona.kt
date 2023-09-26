package com.daniel.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Persona(
    val nombre: String,
    val apellido: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)