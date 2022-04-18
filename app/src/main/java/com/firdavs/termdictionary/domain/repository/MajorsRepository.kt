package com.firdavs.termdictionary.domain.repository

import com.firdavs.termdictionary.domain.model.Major
import kotlinx.coroutines.flow.Flow

interface MajorsRepository {

    fun getMajors(): Flow<List<Major>>
}