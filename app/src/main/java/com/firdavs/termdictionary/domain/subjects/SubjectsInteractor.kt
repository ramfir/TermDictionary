package com.firdavs.termdictionary.domain.subjects

import com.firdavs.termdictionary.domain.repository.SubjectsRepository

class SubjectsInteractor(private val repository: SubjectsRepository) {

    fun getSubjects() = repository.getSubjects()
}