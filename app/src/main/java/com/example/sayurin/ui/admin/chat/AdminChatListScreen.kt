package com.example.sayurin.ui.admin.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminChatListScreen(
    onRoomClick: (Int) -> Unit,
    viewModel: AdminChatListViewModel = hiltViewModel()
) {
    val chatRooms = viewModel.chatRooms

    LaunchedEffect(Unit) {
        viewModel.listenToChatRooms()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBF8))
    ) {
        if (chatRooms.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Belum ada pesan masuk",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Text(
                        text = "Pesan Masuk",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(chatRooms) { room ->
                    val timeString = if (room.last_timestamp > 0) {
                        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                        sdf.format(Date(room.last_timestamp))
                    } else ""

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRoomClick(room.user_id) }
                            .background(Color.White)
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                Text(
                                    text = room.sender_name.ifBlank { "Pembeli ${room.user_id}" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = room.last_message,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            },
                            trailingContent = {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = timeString,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}