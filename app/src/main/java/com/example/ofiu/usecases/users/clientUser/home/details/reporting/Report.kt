package com.example.ofiu.usecases.users.clientUser.home.details.reporting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ofiu.R


@Composable
fun ReportApp(navHostController: NavHostController,
                idPro:String?,
              viewModel: ReportViewModel = hiltViewModel()){

    val text: String by viewModel.report.observeAsState(initial = "")
    val context = LocalContext.current
    Scaffold(
        topBar = {
            ReportTopbar(navHostController = navHostController, text){
                viewModel.reportUser(navHostController, idPro, context)
            }
        }
    ) {
        ReportContentApp(
            modifier = Modifier.padding(it),
            text = text
        ){text ->
            viewModel.onTextChange(text)
        }
    }

}

@Composable
fun ReportTopbar(
    navHostController: NavHostController,
    text: String,
    report: ()->Unit
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
                    navHostController.popBackStack()
                }, enabled = true) {
                    Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
                }
            }
            TextButton(onClick = {
                                 report()
            }, enabled = text.isNotEmpty()
            ) {
                Text(
                    text = "Reportar",
                    style = MaterialTheme.typography.subtitle1,
                    color = if(text.isNotEmpty()){
                        MaterialTheme.colors.onSurface
                    }else{
                        MaterialTheme.colors.background
                    }
                )
            }
        }
    }
}

@Composable
fun ReportContentApp(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit
){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.onPrimary)
        .padding(horizontal = 20.dp, vertical = 10.dp)){
       Column{
           ReportTitle()
           Spacer(modifier = Modifier.height(10.dp))
           TextFiledReport(text = text, onTextChange = { onTextChange(it) } )
       }
    }
}

@Composable
fun ReportTitle(){
    Text(
        text = "¿Por qué estás reportando este perfil?",
        style = MaterialTheme.typography.h2.copy(
            fontSize = 17.sp
        ),
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
fun TextFiledReport(
    text: String,
    onTextChange: (String)->Unit
){
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(value = text,
            onValueChange = { onTextChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textStyle = MaterialTheme.typography.subtitle1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.secondaryVariant
            )
        )
    }
}
