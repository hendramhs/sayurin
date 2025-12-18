package com.example.sayurin.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach

// Toast shortcut
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Convert Flow<T> to State<T> for Compose
suspend fun <T> Flow<T>.collectToState(state: MutableState<T>) {
    this.collectLatest { value ->
        state.value = value
    }
}

// Create MutableState<T> with cleaner syntax
fun <T> stateOf(value: T): MutableState<T> = mutableStateOf(value)

// Trimmed text for TextFieldValue
val TextFieldValue.trimmed: String
    get() = this.text.trim()

// Boolean toggle helper
fun MutableState<Boolean>.toggle() {
    this.value = !this.value
}
