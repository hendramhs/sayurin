package com.example.sayurin.ui.admin.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.sayurin.data.model.ChatRoom
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminChatListViewModel @Inject constructor() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val chatRoomsRef = database.getReference("chat_rooms")
    private val usersRef = database.getReference("users")

    private val _chatRooms = mutableStateListOf<ChatRoom>()
    val chatRooms: List<ChatRoom> = _chatRooms

    private var chatRoomsListener: ValueEventListener? = null

    fun listenToChatRooms() {
        chatRoomsListener?.let { chatRoomsRef.removeEventListener(it) }

        chatRoomsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempRooms = mutableListOf<ChatRoom>()
                snapshot.children.forEach { roomSnapshot ->
                    val room = roomSnapshot.getValue(ChatRoom::class.java)
                    if (room != null) {
                        tempRooms.add(room)
                    }
                }

                val sortedRooms = tempRooms.sortedByDescending { it.last_timestamp }

                _chatRooms.clear()
                _chatRooms.addAll(sortedRooms)

                syncRealNames()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        chatRoomsRef.addValueEventListener(chatRoomsListener!!)
    }

    private fun syncRealNames() {
        _chatRooms.forEachIndexed { index, room ->
            if (room.user_id != 0) {
                usersRef.child(room.user_id.toString()).child("nama")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val realName = snapshot.value?.toString()
                        if (!realName.isNullOrEmpty()) {
                            if (_chatRooms[index].sender_name != realName) {
                                _chatRooms[index] = _chatRooms[index].copy(sender_name = realName)
                            }
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatRoomsListener?.let { chatRoomsRef.removeEventListener(it) }
    }
}