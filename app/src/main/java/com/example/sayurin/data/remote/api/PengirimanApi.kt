package com.example.sayurin.data.remote.api

import com.example.sayurin.data.remote.dto.pengiriman.*
import com.example.sayurin.data.remote.dto.sayur.CommonResponse // Import ini jika belum ada
import com.example.sayurin.utils.Constants
import retrofit2.http.Body
import retrofit2.http.POST

interface PengirimanApi {
    @POST(Constants.HITUNG_ONGKIR)
    suspend fun hitungOngkir(@Body request: OngkirRequest): OngkirResponse

    @POST(Constants.HITUNG_ONGKIR_V2)
    suspend fun hitungOngkirV2(@Body request: OngkirRequestV2): OngkirResponseV2

    @POST(Constants.PILIH_PENGIRIMAN)
    suspend fun pilihPengiriman(@Body request: PilihPengirimanRequest): PilihPengirimanResponse
}
