package com.example.ofiu.usecases.users.clientUser.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R
import com.example.ofiu.remote.dto.ofiu.professionals.User
import com.example.ofiu.usecases.navigation.AppScreens

@Composable
fun HomeApp(navHostController: NavHostController, viewModel: UserHomeProfileViewModel = hiltViewModel()) {
    val searchString: String by viewModel.searchString.observeAsState(initial = "")
    val focus = LocalFocusManager.current
    Scaffold(
        topBar = {
            HomeTopBar(
                value = searchString,
                searchPro = {
                    viewModel.onSearchButton(searchString)
                    focus.clearFocus()
                }
            ) {
                viewModel.onTextChange(it)
                viewModel.onSearchButton(searchString)
            }
        }
    ) {
        HomeContentApp(modifier = Modifier.padding(it), navHostController, viewModel = viewModel)
    }
}

@Composable
fun HomeTopBar(
    searchPro: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(start = 5.dp, bottom = 10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(7f)
                .padding(0.dp),
            value = value,
            onValueChange = { onValueChange(it) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MaterialTheme.colors.onSurface,
                focusedBorderColor = MaterialTheme.colors.primaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.onSurface
            ),
            textStyle = MaterialTheme.typography.subtitle1.copy(
                fontSize = 16.sp,
                color = MaterialTheme.colors.secondaryVariant
            ),
            trailingIcon = {
                if (value.isNotBlank()) {
                    IconButton(onClick = {
                        onValueChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Borrar texto",
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.UserHomeTopbarPlace),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 14.sp,
                    ),
                    color = MaterialTheme.colors.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                searchPro()
            })

        )
        IconButton(
            onClick = {
                searchPro()
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscador",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun HomeContentApp(
    modifier: Modifier,
    navHostController: NavHostController,
    viewModel: UserHomeProfileViewModel
) {
    val users: List<User> by viewModel.users.observeAsState(initial = arrayListOf())
    val isLoading : Boolean by viewModel.isLoading.observeAsState(initial = false)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
        if (isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.background)

            }
        }else{
          LazyColumnPro(users =  users){
              navHostController.navigate(AppScreens.DetailsPro.route + "/${it}")
          }
        }
    }
}

@Composable
fun LazyColumnPro(
  users: List<User>,
  onClickViewPerfil: (String)-> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 5.dp).padding(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(users) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.onPrimary)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (it.imgPerfil != "0"){
                            AsyncImage(model = it.imgPerfil, contentDescription = "foto perfil",
                                modifier = Modifier.size(50.dp),
                            contentScale = ContentScale.Crop)
                        }else{
                            Icon(imageVector = Icons.Default.Person, contentDescription = "foto perfil",
                                modifier = Modifier.size(50.dp))
                        }
                        Column(
                            modifier = Modifier.padding(10.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = it.nombre,
                                    style = MaterialTheme.typography.h2.copy(
                                        fontSize = 16.sp
                                    ),
                                    color = MaterialTheme.colors.secondaryVariant
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = it.estrellas,
                                    style = MaterialTheme.typography.subtitle1.copy(
                                        fontSize = 14.sp
                                    ),
                                    color = MaterialTheme.colors.onBackground
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colors.background
                                )
                            }
                        }
                    }
                    Text(
                        text = it.descProfesional,
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.secondaryVariant,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = it.etiqueta,
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Button(onClick = {onClickViewPerfil(it.idProfesional)},
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.background
                            )
                        ) {
                            Text(
                                text = "Ver perfil",
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}