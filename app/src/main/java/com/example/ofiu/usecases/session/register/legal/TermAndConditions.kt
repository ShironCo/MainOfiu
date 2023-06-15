package com.example.ofiu.usecases.session.register.legal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R


@Composable
fun TermAndConditionsApp(navHostController: NavHostController, viewModel: LegalViewModel = hiltViewModel()){

    val expandMenu: Boolean by viewModel.expandMenu.observeAsState(initial = false)

    Scaffold(
        topBar = { TopBarlegal(
            title = R.string.termAndConditions,
            onPopback = {
                        navHostController.popBackStack()
            },
            iconExpand = {
                         viewModel.expandMenu
            },
            expandMenu = expandMenu
        ) {

        }
        }
    ) {
        TermAndConditionContent(modifier = Modifier.padding(it))
    }
}

@Composable
fun TopBarlegal(
    title: Int,
    onPopback: ()->Unit,
    iconExpand: ()->Unit,
    expandMenu: Boolean,
    dropItem: ()->Unit
){

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
                       onPopback()
                    }, enabled = true) {
                        Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = title),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.secondary
                    )
                }
                IconButton(onClick = {
                    iconExpand()
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = "Opciones de perfil",
                        tint = MaterialTheme.colors.onPrimary
                    )
                    DropdownMenu(
                        expanded = expandMenu,
                        onDismissRequest = {
                            iconExpand()
                                           },
                        modifier = Modifier.background(MaterialTheme.colors.onPrimary)
                    ) {
                        DropdownMenuItem(onClick = {
                            dropItem()
                        }) {
                            Text(
                                text = "Abrir en el navegador",
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
fun TermAndConditionContent(
    modifier: Modifier
){
    Column(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        //Text(text =
       // )

    }
}