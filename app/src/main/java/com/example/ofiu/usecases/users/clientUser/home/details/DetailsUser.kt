package com.example.ofiu.usecases.users.clientUser.home.details

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ofiu.R
import com.example.ofiu.remote.dto.ofiu.professionals.details.comments.Comentario


@Composable
fun DetailsUserApp(
    id: String, navController: NavHostController,
    viewModel: DetailsUserViewModel = hiltViewModel()
) {
    viewModel.setId(id)
    println(id)
    viewModel.getDatUser(id)
    val expandMenu: Boolean by viewModel.expandMenu.observeAsState(initial = false)
    val imagePreview: Uri by viewModel.imagePreview.observeAsState(initial = Uri.EMPTY)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val commentToggle: Boolean by viewModel.commentToggle.observeAsState(initial = false)
    val rating: Int by viewModel.rating.observeAsState(initial = 0)
    val opinion: String by viewModel.opinion.observeAsState(initial = " ")
    val imageProfile: Uri by viewModel.imageProfile.observeAsState(initial = Uri.EMPTY)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
        Scaffold(
            topBar = {
                DetailsTopBar(
                    navController = navController,
                    viewModel,
                    expandMenu,
                    imagePreview
                )
            },
            floatingActionButton = {
                FloatingActionButton(){
                viewModel.onClickChat(navController)
            } },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.onSurface)
                }
            } else {
                DetailsUserContent(Modifier.padding(it), id = id, expandMenu, viewModel)
            }
        }
        if (imagePreview.toString().isNotEmpty()) {
            ImagePreview(uri = imagePreview) {
                viewModel.setData(expandMenu, Uri.EMPTY)
            }
        }
        if (commentToggle) {
            DialogComment(
                ratint = rating,
                onRatingChanged = {
                                  viewModel.setRating(it)
                },
                value = opinion,
                dismiss = {
                    viewModel.changeToggleComment()
                    viewModel.onTextChange("")
                    viewModel.setRating(0)
                },
                onValueChange = {
                   viewModel.onTextChange(it)
                }){
                viewModel.onClickPublicar(
                    rating = rating,
                    idPro = id,
                    desc = opinion
                )
            }
        }
    }
}

@Composable
fun FloatingActionButton(
    onClickChat: ()-> Unit
){
    Button(onClick = {onClickChat()},
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.background
        )
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chat",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colors.onPrimary
            )
        }
        }
}

@Composable
fun DetailsTopBar(
    navController: NavHostController, viewModel: DetailsUserViewModel,
    expandMenu: Boolean,
    imagePreview: Uri
) {

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }, enabled = true) {
                    Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Perfil profesional",
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.secondary
                )
            }
            IconButton(onClick = { viewModel.setData(value = true, imagePreview = imagePreview) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "Opciones de perfil",
                    tint = MaterialTheme.colors.onPrimary
                )
                DropdownMenu(
                    expanded = expandMenu,
                    onDismissRequest = { viewModel.setData(false, imagePreview = imagePreview) },
                    modifier = Modifier.background(MaterialTheme.colors.onPrimary)
                ) {
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Reportar perfil",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onError
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsUserContent(
    modifier: Modifier,
    id: String,
    expandMenu: Boolean,
    viewModel: DetailsUserViewModel
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        DetailsProfile(viewModel)
        DetailsGallery(expandMenu, viewModel)
        DetailsComments(viewModel)
    }
}

@Composable
fun DetailsProfile(viewModel: DetailsUserViewModel) {

    val name: String by viewModel.name.observeAsState(initial = "")
    val imageProfile: Uri by viewModel.imageProfile.observeAsState(initial = Uri.EMPTY)
    val starts: String by viewModel.starts.observeAsState(initial = "")
    val desc: String by viewModel.desc.observeAsState(initial = "")

    Surface(
        color = MaterialTheme.colors.onPrimary,
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .size(100.dp)
                    .background(MaterialTheme.colors.background)
            ) {
                if (imageProfile.toString() != "0") {
                    AsyncImage(
                        model = imageProfile, contentDescription = null,
                        contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null, tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 22.sp,
                    shadow = Shadow(
                        MaterialTheme.colors.background,
                        Offset(1f, 1f),
                        blurRadius = 2f
                    )
                ),
                color = MaterialTheme.colors.primaryVariant,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = starts,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.onBackground,
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24),
                    contentDescription = null, tint = MaterialTheme.colors.background,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
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
            }
        }
    }
}

@Composable
fun DetailsGallery(expandMenu: Boolean, viewModel: DetailsUserViewModel) {

    val imagesGallery: List<Uri?> by viewModel.imageGallery.observeAsState(initial = emptyList())

    Surface(
        color = MaterialTheme.colors.onPrimary,
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.profileWorkerGalleryTitle),
                style = MaterialTheme.typography.h2.copy(
                    fontSize = 17.sp
                ),
                color = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier.padding(start = 10.dp)
            )
            LazyRowItems(image = imagesGallery) {
                viewModel.setData(expandMenu, it)
            }
        }
    }
}

@Composable
fun LazyRowItems(
    image: List<Uri?>,
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
            }
        }
    }
}

@Composable
fun DetailsComments(
    viewModel: DetailsUserViewModel
) {
    val cantComments: String by viewModel.cantComments.observeAsState(initial = "")
    val comment: List<Comentario> by viewModel.comments.observeAsState(emptyList())
    Surface(
        color = MaterialTheme.colors.onPrimary,
        elevation = 4.dp,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.comments),
                        style = MaterialTheme.typography.h2.copy(
                            fontSize = 17.sp
                        ),
                        color = MaterialTheme.colors.secondaryVariant,
                    )
                    Text(
                        text = "$cantComments reseñas",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                Button(
                    onClick = {
                              viewModel.changeToggleComment()
                              },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onPrimary
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Text(
                        text = "Comentar",
                        color = MaterialTheme.colors.background
                    )
                }
            }
            if (comment.isNotEmpty()) {
                if (comment.size < 3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        LazyColumnComments(comments = comment)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        LazyColumnComments(comments = comment)
                    }
                }
            }
        }
    }
}


@Composable
fun LazyColumnComments(
    comments: List<Comentario>
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(comments) {
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .size(30.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        if (it.imgPerfil != "0") {
                            AsyncImage(
                                model = it.imgPerfil, contentDescription = null,
                                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null, tint = MaterialTheme.colors.onSurface,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = it.nombre,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colors.secondaryVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = it.fechadecomentario,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = it.estrella,
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp
                            ),
                            color = MaterialTheme.colors.onBackground,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_star_24),
                            contentDescription = null, tint = MaterialTheme.colors.background,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Text(
                    text = it.comentario,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

//       StarRating(rating = rating, onRatingChanged = {
//           viewModel.setRating(it)
//       })

@Composable
fun StarRating(maxRating: Int = 5, rating: Int, onRatingChanged: (Int) -> Unit) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        for (i in 1..maxRating) {
            val isSelected = i <= rating
            val icon = if (isSelected) {
                Icons.Default.Star
            } else {
                Icons.Default.StarBorder
            }
            IconToggleButton(
                checked = isSelected,
                onCheckedChange = {
                    val newRating = if (isSelected) i - 1 else i
                    onRatingChanged(newRating)
                }
            ) {
                val color =
                    if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.background
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    tint = color
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

@Composable
fun DialogComment(
    ratint: Int,
    onRatingChanged: (Int) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    dismiss: () -> Unit,
    onClickButton: () -> Unit
) {
    val scrollState = rememberScrollState()
    Dialog(onDismissRequest = {
        dismiss()
    }) {
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .height(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.onPrimary)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                ) {
                    Text(
                        "Valorar este perfil",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        textAlign = TextAlign.Start
                    )
                    StarRating(rating = ratint,
                        onRatingChanged = {onRatingChanged(it)})
                    OutlinedTextField(
                        value = value,
                        onValueChange = { onValueChange(it) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colors.onBackground,
                            focusedBorderColor = MaterialTheme.colors.background,
                            unfocusedBorderColor = MaterialTheme.colors.onBackground
                        ),
                        placeholder = {
                            Text(
                                text = "Describe tu experiencia (opcional)",
                                color = MaterialTheme.colors.onBackground
                            )
                        },
                        maxLines = 5,
                        modifier = Modifier.verticalScroll(scrollState),
                        textStyle = MaterialTheme.typography.subtitle1
                    )
                }
                TextButton(
                    onClick = {onClickButton()},
                    enabled = ratint > 0,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.background,
                        disabledContentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(
                        text = "publicar",
                    )
                }
            }
        }
    }
}