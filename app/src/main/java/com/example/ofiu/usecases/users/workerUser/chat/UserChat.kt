package com.example.ofiu.usecases.users.workerUser.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R
import com.example.ofiu.usecases.users.clientUser.chat.ChatAppContent
import com.example.ofiu.usecases.users.clientUser.chat.ChatAppTopbar
import com.example.ofiu.usecases.users.clientUser.chat.UserChatViewModel
import com.example.ofiu.usecases.users.clientUser.chat.dto.Chats


@Composable
fun ChatWorkerApp(navHostController: NavHostController,
                  backTopBar: String?,
                  viewModel: UserWorkerChatViewModel = hiltViewModel()
) {

    val chats: List<Chats> by viewModel.chats.observeAsState(initial = emptyList())

        ChatAppContent(modifier = Modifier, chats) {
            viewModel.onClickChat(
                navHostController,
                idUser = it.id,
                name = it.name,
                imageProfile = it.imageProfile
            )
        }
    }

