package com.example.ofiu.usecases.users.workerUser.profile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ofiu.R
import kotlin.contracts.contract

@Composable
fun ProfileWorkerApp(viewModel: UserProfileViewModel = hiltViewModel()) {
    val toggleDesc: Boolean by viewModel.toggleDesc.observeAsState(initial = false)
    val desc: String by viewModel.desc.observeAsState(initial = "")
    val descDialog: String by viewModel.descDialog.observeAsState(initial = "")
    val loading : Boolean by viewModel.isLoading.observeAsState(initial = false)
    val context = LocalContext.current
    val response : String by viewModel.response.observeAsState(initial = "")
    ProfileWorkerContent(viewModel, desc)
    if (toggleDesc) {
        DescProfileContent(
            response = response,
            loading = loading,
            desc = descDialog,
            changeDesc = {
                viewModel.onTextChange(it)
            },
            onGenerate = {
                viewModel.onGenerateDesc(context)
            },
            dismiss = {
                viewModel.onSetToggleDesc(false)
            }
        )
    }
}

@Composable
fun ProfileWorkerContent(viewModel: UserProfileViewModel, desc: String) {
    val scrollBar = rememberScrollState()

    Column(
        Modifier
            .background(MaterialTheme.colors.onPrimary)
            .fillMaxSize()
            .verticalScroll(scrollBar)
    ) {
        ProfileBasicInformation(viewModel)
        ProfileContactInformation(viewModel)
        ProfileBriefcase(viewModel, desc)
        ProfileGallery(viewModel)
    }
}

@Composable
fun ProfileBasicInformation(viewModel: UserProfileViewModel) {
    val name : String by viewModel.name.observeAsState(initial = "")

    val image : Uri by viewModel.imageProfile.observeAsState(initial = Uri.EMPTY)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()){
        if (it != null){
            viewModel.setImageProfile(it)
        }
    }

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
                    .size(70.dp)
                    .background(MaterialTheme.colors.onSurface)
            ) {
                if (image.toString().isNotBlank()){
                AsyncImage(model = image, contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                        launcher.launch("image/*")
                    })}
                else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null, tint = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            launcher.launch("image/*")
                        })}
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
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
fun ProfileContactInformation(viewModel:UserProfileViewModel) {
    val email : String by viewModel.email.observeAsState(initial = "")
    val phone : String by viewModel.phone.observeAsState(initial = "")
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
                text = phone,
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
            text = email,
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
fun ProfileBriefcase(viewModel: UserProfileViewModel, desc: String) {
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
            text = desc,
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 14.sp
            ),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(bottom = 20.dp)
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
fun DescProfileContent(
    response : String,
    loading: Boolean,
    desc: String,
    changeDesc: (String) -> Unit,
    onGenerate: () -> Unit,
    dismiss: () -> Unit
) {
    val scrollBar = rememberScrollState()
    Dialog(onDismissRequest = {
        if (!loading){
            dismiss()
        }
    }
    ) {
        if (!loading){
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                elevation = 8.dp,
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colors.onSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = desc,
                        onValueChange = {
                            changeDesc(it)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.profileWorkerGenerateDesc),
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 14.sp
                                ), color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(scrollBar),
                        textStyle = TextStyle.Default,
                        maxLines = Int.MAX_VALUE,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                    Text(text = response)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .weight(1f)
                                .clip(MaterialTheme.shapes.small),
                            border = BorderStroke(1.dp, MaterialTheme.colors.background),
                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                onGenerate()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onSurface,
                            ),
                            enabled = true
                        ) {
                            Text(
                                text = "Generar",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colors.background,
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
        }else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(color = Color.White)

            }
        }
    }
}

@Composable
fun ProfileGallery(viewModel: UserProfileViewModel) {
    val context = LocalContext.current
    val images: List<Uri?> by viewModel.imageGallery.observeAsState(emptyList())
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents(),
        onResult = { uris: List<Uri> ->
            uris?.let {
                viewModel.setImages(uris)
                viewModel.onSendImage(context)
            }

        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.profileWorkerGalleryTitle),
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 17.sp
                ),
                color = MaterialTheme.colors.secondaryVariant,
            )
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                contentPadding = PaddingValues(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar imagen")
            }
        }
        LazyRowItems(image = images)
    }
}


@Composable
fun LazyRowItems(image: List<Uri?>) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(image) {
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = 4.dp
            ) {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}