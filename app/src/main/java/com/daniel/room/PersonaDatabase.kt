package com.daniel.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Persona::class],
    version = 1
)
abstract class PersonaDatabase: RoomDatabase() {
    abstract val dao: PersonaDao
}