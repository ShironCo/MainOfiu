package com.example.ofiu.usecases.users.clientUser.chat


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.usecases.users.clientUser.chat.dto.Chats
import com.example.ofiu.R


@Composable
fun ChatApp(navHostController: NavHostController,
            backTopBar: String?,
            viewModel: UserChatViewModel = hiltViewModel()
) {
    val chats: List<Chats> by viewModel.chats.observeAsState(initial = emptyList())
    Scaffold(
        topBar = {
            ChatAppTopbar(backTopBar = backTopBar.toBoolean()) {
                navHostController.popBackStack()
            }
        }
    ) { it ->
        ChatAppContent(modifier = Modifier.padding(it), chats) {
            viewModel.onClickChat(
                navHostController,
                idPro = it.idRecibe,
                name = it.name,
                imageProfile = it.imageProfile
            )
        }
    }
}

@Composable
fun ChatAppTopbar(
    backTopBar: Boolean,
    onClickBack: () -> Unit
) {
    TopAppBar(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        if (backTopBar) {
            IconButton(onClick = {
                onClickBack()
            }, enabled = true) {
                Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
            }
        }
            Text(
            text = "Bandeja de entrada",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

@Composable
fun ChatAppContent(
    modifier: Modifier,
    chatLists: List<Chats>,
    onClickChat: (Chats) ->Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    )
    if (chatLists.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.chat), contentDescription = null)
            Text(
                text = "Aun no hay mensajes",
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colors.secondaryVariant
            )
            Text(
                text = "Encontrarás todas tus conversaciones aquí",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumnChat(chatLists = chatLists) {
            onClickChat(it)
        }
    }
}

@Composable
fun LazyColumnChat(
    chatLists: List<Chats>,
    onClickChat: (Chats) -> Unit
) {
    LazyColumn(
    ) {
        items(chatLists) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.onPrimary)
                    .clickable {
                        onClickChat(it)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(MaterialTheme.colors.onSurface)
                ) {
                    if (it.imageProfile != "0") {
                        AsyncImage(
                            model = it.imageProfile, contentDescription = null,
                            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null, tint = MaterialTheme.colors.background,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.h2.copy(
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colors.secondaryVariant
                    )
                    Text(
                        text = it.previewMessage.ifEmpty{"Escribe un mensaje"} ,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.widthIn(min = 0.dp, max = 300.dp)
                    )
                    Text(
                        text = it.lastMinute,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
    }
}