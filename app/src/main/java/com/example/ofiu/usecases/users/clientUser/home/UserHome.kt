package com.example.ofiu.usecases.users.clientUser.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.ofiu.R
import com.example.ofiu.remote.dto.ofiu.professionals.DataPro

@Composable
fun HomeApp(viewModel: UserHomeProfileViewModel = hiltViewModel()) {
    val searchString: String by viewModel.searchString.observeAsState(initial = "")
    val focus = LocalFocusManager.current
    Scaffold(
        topBar = {
            HomeTopBar(
                value = searchString,
                searchPro = {
                    focus.clearFocus()
                }
            ) {
                viewModel.onTextChange(it)
            }
        }
    ) {
        HomeContentApp(modifier = Modifier.padding(it), focus = focus)
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
    focus: FocusManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
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
                    Image(
                        painter = painterResource(id = R.drawable.face),
                        contentDescription = null, Modifier.size(50.dp)
                    )
                    Column(
                        modifier = Modifier.padding(10.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Juan Pedro",
                                style = MaterialTheme.typography.h2.copy(
                                    fontSize = 18.sp
                                ),
                                color = MaterialTheme.colors.secondaryVariant
                            )
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "4.3",
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
                    text = "KADAKDANSDKADKNADKADNKKADAKDANSDKADKNADKADNKKADAKDANSDKADKNADKADNKKADAKDANSDKADKNADKADNKKADAKDANSDKADKNADKADNK",
                    style = MaterialTheme.typography.body2,
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
                        text = "Mecanico",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.primaryVariant
                    )
                    Button(onClick = { /*TODO*/ },
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

@Composable
fun LazyColumnPro(
    users: List<DataPro>
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(users) {
            Card(
                modifier = Modifier
                    .height(500.dp)
                    .fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(

                ) {

                }
            }
        }
    }
}