package com.example.sayurin.ui.admin.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sayurin.data.local.UserPreferences
import com.example.sayurin.data.model.Message
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminChatViewModel @Inject constructor(
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val chatRoomsRef = database.getReference("chat_rooms")
    // usersRef tetap ada jika dibutuhkan untuk keperluan lain, tapi tidak untuk nama di sini

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    var buyerName by mutableStateOf("Memuat...")
        private set

    private var messageListener: ValueEventListener? = null
    private var nameListener: ValueEventListener? = null

    /**
     * PERBAIKAN: Mengambil nama dari 'chat_rooms/room_ID_admin/sender_name'
     * Lokasi ini terbukti memiliki data (seperti "Hendra" atau "Pasha" di screenshot Anda)
     */
    fun fetchBuyerName(userId: Int) {
        if (userId <= 0) return

        val roomId = "room_${userId}_admin"
        val nameRef = chatRoomsRef.child(roomId).child("sender_name")

        // Bersihkan listener lama
        nameListener?.let { nameRef.removeEventListener(it) }

        nameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.value?.toString()
                // Jika sender_name ada di chat_rooms, tampilkan. Jika tidak, tampilkan fallback.
                buyerName = if (!name.isNullOrBlank()) name else "Pembeli $userId"
            }
            override fun onCancelled(error: DatabaseError) {
                buyerName = "Pembeli $userId"
            }
        }
        nameRef.addValueEventListener(nameListener!!)
    }

    fun listenToMessages(targetUserId: Int) {
        if (targetUserId <= 0) return

        val roomId = "room_${targetUserId}_admin"
        val currentChatRef = chatRoomsRef.child(roomId).child("messages")

        messageListener?.let { currentChatRef.removeEventListener(it) }

        messageListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _messages.clear()
                snapshot.children.forEach { child ->
                    child.getValue(Message::class.java)?.let { _messages.add(it) }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        currentChatRef.orderByChild("timestamp").addValueEventListener(messageListener!!)
    }

    fun sendMessage(text: String, targetUserId: Int) {
        if (text.isBlank() || targetUserId <= 0) return

        viewModelScope.launch {
            val adminId = userPrefs.userId.first() ?: 0
            val roomId = "room_${targetUserId}_admin"
            val timestamp = System.currentTimeMillis()

            val message = Message(
                sender_id = adminId,
                sender_role = "admin",
                message = text,
                timestamp = timestamp
            )

            val dbRef = chatRoomsRef.child(roomId)
            dbRef.child("messages").push().setValue(message)

            // Update metadata agar AdminChatListScreen tetap terupdate
            val updates = mapOf(
                "last_message" to text,
                "last_timestamp" to timestamp
            )
            dbRef.updateChildren(updates)
        }
    }

    override fun onCleared() {
        super.onCleared()
        messageListener?.let { chatRoomsRef.removeEventListener(it) }
        // Pastikan nameListener dibersihkan dari path yang benar
        nameListener?.let {
            // Kita tidak perlu path spesifik karena listener di-attach ke nameRef yang dinamis
            // Namun secara umum removeEventListener akan bekerja jika objek listener-nya sama
            chatRoomsRef.removeEventListener(it)
        }
    }
}