package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.HitungOngkirRequest
import com.example.sayurin.data.remote.dto.HitungOngkirResponse
import com.example.sayurin.data.remote.dto.PilihPengirimanRequest
import com.example.sayurin.data.remote.dto.PilihPengirimanResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PengirimanApi {

    @POST("/api/pengiriman/hitung")
    suspend fun hitungOngkir(
        @Body request: HitungOngkirRequest
    ): HitungOngkirResponse

    @POST("/api/pengiriman/pilih")
    suspend fun pilihPengiriman(
        @Body request: PilihPengirimanRequest
    ): PilihPengirimanResponse
}
