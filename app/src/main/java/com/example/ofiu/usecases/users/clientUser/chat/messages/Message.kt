package com.example.ofiu.usecases.users.clientUser.chat.messages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R
import com.example.ofiu.usecases.users.clientUser.chat.dto.MessageChat

@Composable
fun MessageApp(
    navHostController: NavHostController,
    idUser: String?,
    idPro: String?,
    name: String?,
    image: String?,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val message: String by viewModel.message.observeAsState(initial = "")
    val current = LocalFocusManager.current
    val messageList: List<MessageChat> by viewModel.listMessages.observeAsState(initial = emptyList())
    val imageProfile = image?.replace("-","/")
    println(imageProfile)
    println(idPro)
    if (idPro != null && imageProfile != null) {
        viewModel.setIdUser(idPro, imageProfile)
        viewModel.newChats(idPro)
    } else {
        navHostController.popBackStack()
    }

    Scaffold(
        topBar = {
            MessageTopBar(
                imageProfile!!.toUri(),
                name
            ) {
                navHostController.popBackStack()
            }
        },
        bottomBar = {
            MessageBottomBar(message, onClickSend = {
                viewModel.onSendMessage(
                    name = name!!,
                    message = message,
                    idUser = idUser!!,
                    idPro = idPro!!
                )
                current.clearFocus()
            }) {
                viewModel.onMessageChange(it)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.onSurface)
        )
        MessageAppContent(
            modifier = Modifier.padding(it),
            messageList = messageList, idUser = idUser)
    }

}

@Composable
fun MessageTopBar(
    imageProfile: Uri,
    name: String?,
    onClickBack: () -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onClickBack()
            }, enabled = true) {
                Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(MaterialTheme.colors.onSurface)
            ) {
                if (imageProfile.toString() != "0") {
                    AsyncImage(
                        model = imageProfile, contentDescription = null,
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
            Text(
                text = name!!,
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun MessageBottomBar(message: String, onClickSend: () -> Unit, onMessageChange: (String) -> Unit) {
    BottomAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier.padding(bottom = 5.dp)
    ) {
        Card(
            modifier = Modifier.weight(9f),
            shape = MaterialTheme.shapes.large,
        ) {
            OutlinedTextField(
                value = message, onValueChange = { onMessageChange(it) },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onBackground,
                    backgroundColor = MaterialTheme.colors.onPrimary,
                ),
                placeholder = {
                    Text(
                        text = "Mensaje",
                        color = MaterialTheme.colors.onBackground
                    )
                },
                textStyle = MaterialTheme.typography.subtitle1
            )
        }
        IconButton(
            onClick = {
                onClickSend()
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Send, contentDescription = null,
                tint = MaterialTheme.colors.background
            )
        }
    }
}


@Composable
fun MessageAppContent(
    idUser: String?,
    modifier: Modifier,
    messageList: List<MessageChat>,
) {
    Box(modifier = Modifier
        .fillMaxSize()) {
        LazyColumnMessage(messageList = messageList, idUser = idUser)
    }
}

@Composable
fun LazyColumnMessage(
    idUser: String?,
    messageList: List<MessageChat>,
) {
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
    ) {
        items(messageList) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                Card(
                    elevation = 0.dp,
                    backgroundColor = if(idUser == it.sender) MaterialTheme.colors.onPrimary else{MaterialTheme.colors.onPrimary},
                    shape = RoundedCornerShape(
                        topStartPercent = 20,
                        topEndPercent = 0,
                        bottomStartPercent = 20,
                        bottomEndPercent = 20
                    ),
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .widthIn(min = 0.dp, max = 350.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(
                            horizontal = 10.dp, vertical = 5.dp
                        )
                    ) {
                        Text(
                            text = it.message,
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colors.secondaryVariant,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = it.timestamp,
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                }
            }
        }
    }
}