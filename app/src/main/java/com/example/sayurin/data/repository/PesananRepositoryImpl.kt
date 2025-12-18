package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.PesananApi
import com.example.sayurin.data.remote.dto.PesananRequest
import com.example.sayurin.domain.repository.PesananRepository
import javax.inject.Inject

class PesananRepositoryImpl @Inject constructor(
    private val api: PesananApi
) : PesananRepository {

    override suspend fun createPesanan(request: PesananRequest) =
        api.createPesanan(request)

    override suspend fun getPesananAdmin() =
        api.getPesananAdmin()

    override suspend fun getDetailPesanan(id: Int) =
        api.getDetailPesanan(id)

    override suspend fun updateStatusPesanan(id: Int, status: String) =
        api.updateStatusPesanan(id, mapOf("status" to status))
}
