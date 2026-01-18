package com.example.sayurin.ui.client.chat

import androidx.compose.runtime.mutableStateListOf
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
class ChatViewModel @Inject constructor(
    private val userPrefs: UserPreferences
) : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val chatRoomsRef = database.getReference("chat_rooms")

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    private var messageListener: ValueEventListener? = null

    fun listenToMessages() {
        viewModelScope.launch {
            val myId = userPrefs.userId.first() ?: return@launch
            val roomId = "room_${myId}_admin"
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
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            val myId = userPrefs.userId.first() ?: return@launch
            val myName = userPrefs.userName.first() ?: "User $myId"
            val roomId = "room_${myId}_admin"
            val timestamp = System.currentTimeMillis()

            val message = Message(
                sender_id = myId,
                sender_role = "client",
                message = text,
                timestamp = timestamp
            )

            val dbRef = chatRoomsRef.child(roomId)
            dbRef.child("messages").push().setValue(message)

            val updates = mapOf(
                "last_message" to text,
                "last_timestamp" to timestamp,
                "sender_name" to myName,
                "user_id" to myId
            )
            dbRef.updateChildren(updates)
        }
    }

    override fun onCleared() {
        super.onCleared()
        messageListener?.let { chatRoomsRef.removeEventListener(it) }
    }
}