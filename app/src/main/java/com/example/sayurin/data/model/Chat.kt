package com.example.sayurin.data.model

data class Message(
    val sender_id: Int = 0,
    val sender_role: String = "", // "admin" atau "client"
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

// data/model/Chat.kt
data class ChatRoom(
    val last_message: String = "",
    val last_timestamp: Long = 0,
    val sender_name: String = "", // Tambahkan ini agar admin tahu siapa yang chat
    val user_id: Int = 0          // Untuk navigasi admin ke detail chat
)