package com.example.sayurin.data.repository

import com.example.sayurin.data.remote.api.PengirimanApi
import com.example.sayurin.data.remote.dto.HitungOngkirRequest
import com.example.sayurin.data.remote.dto.PilihPengirimanRequest
import com.example.sayurin.domain.repository.PengirimanRepository
import javax.inject.Inject

class PengirimanRepositoryImpl @Inject constructor(
    private val api: PengirimanApi
) : PengirimanRepository {

    override suspend fun hitungOngkir(request: HitungOngkirRequest) =
        api.hitungOngkir(request)

    override suspend fun pilihPengiriman(request: PilihPengirimanRequest) =
        api.pilihPengiriman(request)
}
