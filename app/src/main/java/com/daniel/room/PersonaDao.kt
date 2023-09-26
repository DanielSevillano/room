package com.daniel.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {
    @Upsert
    suspend fun insertarPersona(persona: Persona)

    @Delete
    suspend fun eliminarPersona(persona: Persona)

    @Query("DELETE FROM persona")
    suspend fun eliminarTodos()

    @Query("SELECT * FROM persona")
    fun obtenerPersonas(): Flow<List<Persona>>
}