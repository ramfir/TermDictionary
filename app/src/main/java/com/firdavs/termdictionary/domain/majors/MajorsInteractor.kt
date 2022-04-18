package com.firdavs.termdictionary.domain.majors

import com.firdavs.termdictionary.domain.repository.MajorsRepository

class MajorsInteractor(private val repository: MajorsRepository) {

    fun getMajors() = repository.getMajors()
}