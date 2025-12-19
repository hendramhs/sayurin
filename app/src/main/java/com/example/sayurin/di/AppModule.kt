package com.example.sayurin.di

import android.content.Context
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.remote.api.AddressApi
import com.example.sayurin.data.remote.api.AuthApi
import com.example.sayurin.data.remote.api.CartApi
import com.example.sayurin.data.remote.api.PengirimanApi
import com.example.sayurin.data.remote.api.PesananApi
import com.example.sayurin.data.remote.api.SayurApi
import com.example.sayurin.data.repository.AddressRepository
import com.example.sayurin.data.repository.CartRepository
import com.example.sayurin.data.repository.PengirimanRepository
import com.example.sayurin.data.repository.PengirimanRepositoryImpl
import com.example.sayurin.data.repository.PesananRepository
import com.example.sayurin.data.repository.PesananRepositoryImpl
import com.example.sayurin.data.repository.SayurRepository
import com.example.sayurin.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Di dalam AppModule.kt
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // <--- Menggunakan yang ada di Constants.kt
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideSayurApi(retrofit: Retrofit): SayurApi =
        retrofit.create(SayurApi::class.java)

    @Provides
    @Singleton
    fun provideSayurRepository(api: SayurApi): SayurRepository =
        SayurRepository(api)

    @Provides
    @Singleton
    fun provideAddressApi(retrofit: Retrofit): AddressApi =
        retrofit.create(AddressApi::class.java)

    @Provides
    @Singleton
    fun provideAddressRepository(api: AddressApi): AddressRepository =
        AddressRepository(api)

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi =
        retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun provideCartRepository(api: CartApi): CartRepository =
        CartRepository(api)

    // --- Pesanan ---
    @Provides
    @Singleton
    fun providePesananApi(retrofit: Retrofit): PesananApi =
        retrofit.create(PesananApi::class.java)

    @Provides
    @Singleton
    fun providePesananRepository(api: PesananApi): PesananRepository =
        PesananRepositoryImpl(api)

    // --- Pengiriman ---
    @Provides
    @Singleton
    fun providePengirimanApi(retrofit: Retrofit): PengirimanApi =
        retrofit.create(PengirimanApi::class.java)

    @Provides
    @Singleton
    fun providePengirimanRepository(api: PengirimanApi): PengirimanRepository =
        PengirimanRepositoryImpl(api)


}
