package com.sanctum.core.core.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DuasDao {
    @Query("SELECT * FROM duas")
    suspend fun getAllDuas(): List<DuaEntity>
}
