package com.example.sayurin.ui.admin.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sayurin.ui.client.chat.ChatBubble
import com.example.sayurin.ui.client.chat.ChatInputArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChatScreen(
    userId: Int,
    onBack: () -> Unit,
    viewModel: AdminChatViewModel = hiltViewModel()
) {
    var textState by remember { mutableStateOf("") }
    val messages = viewModel.messages
    val buyerName = viewModel.buyerName
    val listState = rememberLazyListState()

    LaunchedEffect(userId) {
        if (userId != 0) {
            viewModel.listenToMessages(userId)
            viewModel.fetchBuyerName(userId)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = buyerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "ID Pembeli: $userId",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isMine = (msg.sender_role == "admin")
                    ChatBubble(
                        message = msg.message,
                        timestamp = msg.timestamp,
                        isMine = isMine
                    )
                }
            }

            ChatInputArea(
                textState = textState,
                onTextChange = { textState = it },
                onSend = {
                    if (textState.isNotBlank()) {
                        viewModel.sendMessage(textState, userId)
                        textState = ""
                    }
                }
            )
        }
    }
}