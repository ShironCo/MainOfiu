package com.example.ofiu.usecases.users.clientUser.chat.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.ofiu.R
import com.example.ofiu.usecases.users.clientUser.chat.dto.MessageChat

@Composable
fun MessageApp(viewModel: MessageViewModel = hiltViewModel()){
    val message: String by viewModel.message.observeAsState(initial = "")

    val messageList: List<MessageChat> = listOf(
        MessageChat("Hola mi perro", "16:20"),
        MessageChat("Hola mi perro", "16:20"),
        MessageChat("Hola mi perro", "16:20"),
        MessageChat("Hola mi perro", "16:20"),
        MessageChat("Hola mi perro", "16:20"),
        MessageChat("Hola mi perro", "16:20"),
        MessageChat("Hola mi perro", "16:20")
    )

    Scaffold(
        topBar = { MessageTopBar()},
        bottomBar = {
            MessageBottomBar(message){
                viewModel.onMessageChange(it)
            }
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface))
        MessageAppContent(modifier = Modifier.padding(it), messageList)
    }

}

@Composable
fun MessageTopBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {

            }, enabled = true) {
                Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
            }
            AsyncImage(model = "https://this-person-does-not-exist.com/img/avatar-genc4e18877b0a6ed87e2ff6469dc82d991.jpg", contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Juan Pablo",
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun MessageBottomBar(message: String, onMessageChange: (String)->Unit) {
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
                value = message, onValueChange = { onMessageChange(it)},
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
        IconButton(onClick = {
        },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null,
            tint = MaterialTheme.colors.background)
        }
    }
}


@Composable
fun MessageAppContent(
    modifier: Modifier,
    messageList: List<MessageChat>
){
    LazyColumnMessage(messageList)
}

@Composable
fun LazyColumnMessage(
    messageList: List<MessageChat>
){
    LazyColumn(
        reverseLayout = true
    ){
        items(messageList){
            Card(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(
                    topStartPercent = 10,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                ),
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 5.dp)
                    .widthIn(min = 0.dp, max = 300.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 5.dp,
                        bottom = 5.dp,
                        end = 5.dp)
                ) {
                    Text(
                        text = it.message,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize =  15.sp
                        ),
                        color = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = it.hora,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}