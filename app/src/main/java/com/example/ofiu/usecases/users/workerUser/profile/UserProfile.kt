package com.example.ofiu.usecases.users.workerUser.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ofiu.R

@Composable
fun ProfileWorkerApp(viewModel: UserProfileViewModel = hiltViewModel()) {
    val toggleDesc: Boolean by viewModel.toggleDesc.observeAsState(initial = false)
    val desc: String by viewModel.desc.observeAsState(initial = "")

    ProfileWorkerContent(viewModel, toggleDesc)
    if (toggleDesc) {
        DescProfileContent(
            desc = desc,
            changeDesc = {
                viewModel.onTextChange(it)
            },
            dismiss = {
                viewModel.onSetToggleDesc(false)
                viewModel.onTextChange("")
            }
        )
    }
}

@Composable
fun ProfileWorkerContent(viewModel: UserProfileViewModel, toggleDesc: Boolean) {
    Column(
        Modifier.background(MaterialTheme.colors.onPrimary)
    ) {
        ProfileBasicInformation()
        ProfileContactInformation()
        ProfileBriefcase(viewModel)
        ProfileGallery()
    }
}

@Composable
fun ProfileBasicInformation() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 8.dp,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = MaterialTheme.colors.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .wrapContentSize()
                    .background(MaterialTheme.colors.onSurface)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null, tint = MaterialTheme.colors.background,
                    modifier = Modifier.size(70.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Juan Pablo",
                    style = MaterialTheme.typography.h2.copy(
                        fontSize = 20.sp,
                        shadow = Shadow(
                            MaterialTheme.colors.onSurface,
                            Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    ),
                    color = MaterialTheme.colors.surface,
                    modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "4.3",
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colors.surface,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_star_24),
                            contentDescription = null, tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "30 Comentarios",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.surface,
                    )
                }
            }
        }

    }
}

@Composable
fun ProfileContactInformation() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(id = R.string.profileWorkerContactTitle),
            style = MaterialTheme.typography.h2.copy(
                fontSize = 18.sp
            ),
            color = MaterialTheme.colors.secondaryVariant
        )
        Text(
            text = "Número de télefono",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 17.sp
            ),
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(top = 15.dp, bottom = 5.dp)
        )
        //PHONE
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+57",
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 15.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                text = "3132126908",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 15.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        Text(
            text = "Este es el numero de contacto, y otras notificaciones.",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 12.sp
            ),
            color = MaterialTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(20.dp))
        //EMAIL
        Text(
            text = "Correo electrónico",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 17.sp
            ),
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
        )
        Text(
            text = "juandafloo55@gmail.com",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 15.sp
            ),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(
            text = "Es importante que verifiques tu email, para poder contactar de forma segura contigo.",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 12.sp
            ),
            color = MaterialTheme.colors.onBackground,
        )
    }
}

@Composable
fun ProfileBriefcase(viewModel: UserProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(id = R.string.profileWorkerBriefcaseTitle),
            style = MaterialTheme.typography.h2.copy(
                fontSize = 17.sp
            ),
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 14.sp
            ),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable {
                    viewModel.onSetToggleDesc(true)
                },
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.background
        ) {
            Text(
                text = stringResource(id = R.string.profileWorkerBriefcaseDesc),
                style = MaterialTheme.typography.h3.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileGallery() {
    Text(
        text = stringResource(id = R.string.profileWorkerGalleryTitle),
        style = MaterialTheme.typography.h2.copy(
            fontSize = 17.sp
        ),
        color = MaterialTheme.colors.secondaryVariant,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun DescProfileContent(
    desc: String,
    changeDesc: (String) -> Unit,
    dismiss: () -> Unit
) {
    Dialog(onDismissRequest = { dismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = 8.dp,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colors.onSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                TextField(
                    value = desc,
                    onValueChange = {
                        changeDesc(it)
                    },
                    placeholder ={
                        Text(
                            text = stringResource(id = R.string.profileWorkerGeneratePlace),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp
                            ), color = MaterialTheme.colors.onBackground
                        )
                    },
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = TextStyle.Default,
                    maxLines = Int.MAX_VALUE,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )
                Text(
                    text = stringResource(id = R.string.profileWorkerGenerateDesc),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Justify
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(1f)
                            .clip(MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                        ),
                        enabled = true
                    ) {
                            Text(
                                text = "Generar",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(5.dp)
                            )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(1f)
                            .clip(MaterialTheme.shapes.small),
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                        ),
                        enabled = true
                    ) {
                            Text(
                                text = "Guardar",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(5.dp)
                            )
                    }
                }
            }
        }
    }
}
