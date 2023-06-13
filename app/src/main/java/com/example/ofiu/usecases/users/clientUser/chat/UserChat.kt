package com.example.ofiu.usecases.users.clientUser.chat


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ofiu.usecases.users.clientUser.chat.dto.Chats
import com.example.ofiu.R



@Composable
fun ChatApp(){

    val chats = listOf<Chats>(
        Chats("https://this-person-does-not-exist.com/img/avatar-genf0daf0e79069f5166ba2b3ad8849e86c.jpg", "Juan Pablo", "Hola como estas", "7/6/23"),
        Chats("https://this-person-does-not-exist.com/img/avatar-genc4e18877b0a6ed87e2ff6469dc82d991.jpg", "Diego Vacca", "Hola como estas", "28/5/23"),
        Chats("https://this-person-does-not-exist.com/img/avatar-gendd2e715c616797e148e9d5bc544fe847.jpg", "Maria Esperanza", "Hola como estas", "24/55/23"),
        Chats("https://this-person-does-not-exist.com/img/avatar-genb0b70948d9afa0333b1991b19c72593e.jpg", "Saul Forero", "Hola como estas", "12/3/23"),
    )

    Scaffold(
        topBar = {ChatAppTopbar()}
    ) {
        ChatAppContent(modifier = Modifier.padding(it), chats){

        }
    }
}

@Composable
fun ChatAppTopbar(){
    TopAppBar(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
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
    onClickChat: ()->Unit
){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.onSurface))
   if (chatLists.isEmpty()){
       Column(
           modifier = Modifier.fillMaxSize().padding(30.dp),
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
           Text(text = "Envia tu primer mensaje, Encontrarás todas tus conversaciones aquí",
               style = MaterialTheme.typography.subtitle1.copy(
                   fontSize = 16.sp
               ),
               color = MaterialTheme.colors.onBackground,
               modifier = Modifier.fillMaxWidth(),
               textAlign = TextAlign.Center
           )
       }

   }else {
       LazyColumnChat(chatLists = chatLists) {
           onClickChat()
       }
   }
}

@Composable
fun LazyColumnChat(
    chatLists: List<Chats>,
    onClickChat: ()->Unit
){
    LazyColumn(){
        items(chatLists){
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.onPrimary)
                    .clickable {

                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                AsyncImage(model =it.imageProfile, contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp),
                    contentScale = ContentScale.Crop
                )
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
                        text = it.previewMessage,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
    }
}