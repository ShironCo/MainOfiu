package com.example.ofiu.usecases.users.workerUser.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.ofiu.R

@Composable
fun ProfileWorkerApp(viewModel: UserProfileViewModel = hiltViewModel()) {
    val toggleDesc: Boolean by viewModel.toggleDesc.observeAsState(initial = false)
    val imagePreviewToggle: Boolean by viewModel.imagePreviewToggle.observeAsState(initial = false)
    val imagePreview: Uri by viewModel.imagePreview.observeAsState(initial = Uri.EMPTY)
    val desc: String by viewModel.desc.observeAsState(initial = "")
    val descDialog: String by viewModel.descDialog.observeAsState(initial = "")
    val descEmpy: Boolean by viewModel.descEmpty.observeAsState(initial = false)
    val loading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val loadingDesc: Boolean by viewModel.isLoadingDesc.observeAsState(initial = false)
    val context = LocalContext.current
    ProfileWorkerContent(viewModel, desc)
    if (toggleDesc) {
        DescProfileContent(
            descEmpy = descEmpy,
            loading = loadingDesc,
            desc = descDialog,
            changeDesc = {
                viewModel.onTextChange(desc, it)
            },
            onGenerate = {
                viewModel.onGenerateDesc(context)
            },
            saveDesc = {
                       viewModel.onClickButtonSave(context)
            },
            dismiss = {
                viewModel.onSetToggleDesc(false)
                viewModel.onTextChange(desc, "")
            }
        )
    }
    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator(color = MaterialTheme.colors.background)
        }
    }

    if (imagePreviewToggle) {
        ImagePreview(imagePreview) {
            viewModel.setImagePreviewToggle()
        }
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
    val context = LocalContext.current
    val name: String by viewModel.name.observeAsState(initial = "")
    val starts: String by viewModel.starts.observeAsState(initial = "")
    val comments: String by viewModel.comments.observeAsState(initial = "")
    val imageProfile: Uri by viewModel.imageProfile.observeAsState(initial = Uri.EMPTY)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            viewModel.setImageProfile(it)
            viewModel.updatePhotoProfile(it, context)
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
                    .size(80.dp), contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30))
                        .size(70.dp)
                        .background(MaterialTheme.colors.onSurface)
                ) {
                    if (imageProfile.toString() != "0") {
                        AsyncImage(model = imageProfile, contentDescription = null,
                            contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                                launcher.launch("image/*")
                            })
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null, tint = MaterialTheme.colors.background,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    launcher.launch("image/*")
                                })
                        }
                    }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .size(20.dp)
                            .background(MaterialTheme.colors.background)
                            .padding(3.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary)
                    }
                    }
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
                            text = starts,
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
                        text = "$comments Comentarios",
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
fun ProfileContactInformation(viewModel: UserProfileViewModel) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val phone: String by viewModel.phone.observeAsState(initial = "")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        Text(
            text = "Este es el numero de contacto, y otras notificaciones.",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 14.sp
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
                fontSize = 16.sp
            ),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(end = 10.dp)
        )
//        Text(
//            text = "Es importante que verifiques tu email, para poder contactar de forma segura contigo.",
//            style = MaterialTheme.typography.subtitle1.copy(
//                fontSize = 14.sp
//            ),
//            color = MaterialTheme.colors.onBackground,
//        )
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
                fontSize = 16.sp
            ),
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Justify,
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
    descEmpy: Boolean,
    loading: Boolean,
    desc: String,
    changeDesc: (String) -> Unit,
    onGenerate: () -> Unit,
    saveDesc: ()-> Unit,
    dismiss: () -> Unit
) {
    val scrollBar = rememberScrollState()
    Dialog(onDismissRequest = {
        if (!loading) {
            dismiss()
        }
    }
    ) {
        if (!loading) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
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
                                    fontSize = 15.sp
                                ), color = MaterialTheme.colors.onBackground
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(scrollBar),
                        textStyle = MaterialTheme.typography.subtitle1.copy(
                            color = MaterialTheme.colors.secondaryVariant
                        ),
                        maxLines = Int.MAX_VALUE,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                    if (!descEmpy) {
                        Text(
                            text = "¡Animate a escribir algo!",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle1,
                        )
                    }
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
                            border =
                            if (descEmpy) {
                                BorderStroke(1.dp, MaterialTheme.colors.background)
                            } else {
                                BorderStroke(1.dp, MaterialTheme.colors.onSurface)
                            },
                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                onGenerate()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onSurface,
                            ),
                            enabled = descEmpy
                        ) {
                            Text(
                                text = "Generar",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 15.sp
                                ),
                                color = if (descEmpy) {
                                    MaterialTheme.colors.background
                                } else {
                                    MaterialTheme.colors.onSurface
                                },
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
                                      saveDesc()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                            ),
                            enabled = descEmpy
                        ) {
                            Text(
                                text = "Guardar",
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontSize = 15.sp
                                ),
                                color = if (!descEmpy) {
                                    MaterialTheme.colors.onSurface
                                } else {
                                    MaterialTheme.colors.secondary
                                },
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        } else {
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
            uris.let {
                viewModel.setImages(uris)
                viewModel.onSendImage(context, uris)
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
        LazyRowItems(image = images,
            deleteImage = { viewModel.onDeleteImage(it, context) },
            imageUri = {viewModel.setImagePreview(it)
        })
    }
}


@Composable
fun LazyRowItems(
    image: List<Uri?>,
    deleteImage: (Uri) -> Unit,
    imageUri: (Uri) -> Unit,
) {
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
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                it?.let { imageUri(it) }
                            },
                        contentScale = ContentScale.Crop
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                it?.let { deleteImage(it) }
                            }
                    )
                }
            }
    }
}

@Composable
fun ImagePreview(uri: Uri, dismiss: () -> Unit) {
    Dialog(onDismissRequest = {
        dismiss()
    }
    ) {
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            AsyncImage(
                model = uri,
                contentDescription = null,
            )
        }
    }
}