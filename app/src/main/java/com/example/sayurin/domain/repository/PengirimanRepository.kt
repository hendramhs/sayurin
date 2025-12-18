package com.example.sayurin.domain.repository

import com.example.sayurin.data.remote.dto.HitungOngkirRequest
import com.example.sayurin.data.remote.dto.HitungOngkirResponse
import com.example.sayurin.data.remote.dto.PilihPengirimanRequest
import com.example.sayurin.data.remote.dto.PilihPengirimanResponse

interface PengirimanRepository {
    suspend fun hitungOngkir(request: HitungOngkirRequest): HitungOngkirResponse
    suspend fun pilihPengiriman(request: PilihPengirimanRequest): PilihPengirimanResponse
}
