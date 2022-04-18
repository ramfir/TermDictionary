package com.firdavs.termdictionary.data.repository

import com.firdavs.termdictionary.data.room.dao.MajorsDao
import com.firdavs.termdictionary.data.room.entity.toDomain
import com.firdavs.termdictionary.domain.model.Major
import com.firdavs.termdictionary.domain.repository.MajorsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MajorsRepositoryImpl(private val dao: MajorsDao): MajorsRepository {

    override fun getMajors(): Flow<List<Major>> = dao.getMajors().map { it.toDomain() }
}