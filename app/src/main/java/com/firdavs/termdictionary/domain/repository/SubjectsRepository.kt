package com.firdavs.termdictionary.domain.repository

import com.firdavs.termdictionary.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectsRepository {

    fun getSubjects(): Flow<List<Subject>>
}