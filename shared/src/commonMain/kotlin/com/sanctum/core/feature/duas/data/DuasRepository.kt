package com.sanctum.core.feature.duas.data

import com.sanctum.core.feature.duas.presentation.Dua

interface DuasRepository {
    suspend fun getDuas(religionId: String): List<Dua>
}
