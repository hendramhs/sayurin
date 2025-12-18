package com.example.sayurin.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.sayurin.data.local.datastore.UserPreferences
import com.example.sayurin.data.remote.api.AuthApi
import com.example.sayurin.data.remote.api.SayurApi
import com.example.sayurin.data.remote.api.PesananApi
import com.example.sayurin.data.remote.api.PengirimanApi
import com.example.sayurin.data.remote.api.AddressApi
import com.example.sayurin.data.repository.AuthRepositoryImpl
import com.example.sayurin.data.repository.SayurRepositoryImpl
import com.example.sayurin.data.repository.PesananRepositoryImpl
import com.example.sayurin.data.repository.AddressRepositoryImpl
import com.example.sayurin.data.repository.PengirimanRepositoryImpl
import com.example.sayurin.domain.repository.AuthRepository
import com.example.sayurin.domain.repository.SayurRepository
import com.example.sayurin.domain.repository.PesananRepository
import com.example.sayurin.domain.repository.AddressRepository
import com.example.sayurin.domain.repository.PengirimanRepository
import com.example.sayurin.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(Constants.DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API interfaces
    @Provides @Singleton fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
    @Provides @Singleton fun provideSayurApi(retrofit: Retrofit): SayurApi = retrofit.create(SayurApi::class.java)
    @Provides @Singleton fun providePesananApi(retrofit: Retrofit): PesananApi = retrofit.create(PesananApi::class.java)
    @Provides @Singleton fun providePengirimanApi(retrofit: Retrofit): PengirimanApi = retrofit.create(PengirimanApi::class.java)
    @Provides @Singleton fun provideAddressApi(retrofit: Retrofit): AddressApi = retrofit.create(AddressApi::class.java)

    // Repositories
    @Provides @Singleton
    fun provideAuthRepository(api: AuthApi): AuthRepository = AuthRepositoryImpl(api)

    @Provides @Singleton
    fun provideSayurRepository(api: SayurApi): SayurRepository = SayurRepositoryImpl(api)

    @Provides @Singleton
    fun providePesananRepository(api: PesananApi): PesananRepository = PesananRepositoryImpl(api)

    @Provides @Singleton
    fun providePengirimanRepository(api: PengirimanApi): PengirimanRepository =
        PengirimanRepositoryImpl(api)

    @Provides @Singleton
    fun provideAddressRepository(api: AddressApi): AddressRepository =
        AddressRepositoryImpl(api)

    // User Preferences (DataStore)
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context.dataStore)
    }
}
