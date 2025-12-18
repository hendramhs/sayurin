package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.SayurApi
import com.example.sayurin.data.remote.dto.SayurDto
import com.example.sayurin.domain.repository.SayurRepository
import javax.inject.Inject

class SayurRepositoryImpl @Inject constructor(
    private val api: SayurApi
) : SayurRepository {

    override suspend fun getSayur() = api.getSayur()

    override suspend fun addSayur(dto: SayurDto) =
        api.addSayur(dto)

    override suspend fun updateSayur(id: Int, dto: SayurDto) =
        api.updateSayur(id, dto)

    override suspend fun deleteSayur(id: Int) =
        api.deleteSayur(id)
}
