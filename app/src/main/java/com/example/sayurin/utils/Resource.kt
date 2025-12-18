package com.example.sayurin.utils

sealed class Resource {
    object Idle : Resource()
    object Loading : Resource()
    data class Success<T>(val data: T) : Resource()
    data class Error(val message: String) : Resource()
}