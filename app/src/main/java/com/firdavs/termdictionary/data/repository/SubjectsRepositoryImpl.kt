package com.firdavs.termdictionary.data.repository

import com.firdavs.termdictionary.data.room.dao.SubjectsDao
import com.firdavs.termdictionary.data.room.entity.toDomain
import com.firdavs.termdictionary.domain.model.Subject
import com.firdavs.termdictionary.domain.repository.SubjectsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectsRepositoryImpl(private val dao: SubjectsDao): SubjectsRepository {

    override fun getSubjects() = dao.getSubjects().map { it.toDomain() }
}