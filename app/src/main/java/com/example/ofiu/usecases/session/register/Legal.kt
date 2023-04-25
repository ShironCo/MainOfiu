package com.example.ofiu.usecases.session.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ofiu.R

@Composable
fun LegalApp(){
    Scaffold(
        topBar = { LegalTopBar() }
    ){paddingValues -> LegalContent(modifier = Modifier.padding(paddingValues))

    }

}


@Composable
fun LegalTopBar(){
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = CenterVertically
            ) {
                IconButton(onClick = {
                }, enabled = true) {
                    Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null,)

                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(id = R.string.policyTitle),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.secondary
                )
            }
        }, backgroundColor = MaterialTheme.colors.background,
    )
}

@Composable
fun LegalContent(modifier:Modifier = Modifier){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 30.dp),
        ) {
            Text(text = stringResource(id = R.string.policy),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondaryVariant
                )
            Spacer(modifier = Modifier.height(20.dp))
            LegalCard(R.string.termAndConditions, R.string.termAndConditionsDesc)
            Spacer(modifier = Modifier.height(16.dp))
            LegalCard(R.string.noticePrivacity, R.string.noticePrivacityDesc)
            Spacer(modifier = Modifier.height(16.dp))
            ButtonLegal(modifier = Modifier.wrapContentWidth(Alignment.Start))
        }
    }
}

@Composable
fun LegalCard(title:Int, desc:Int){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 9.dp,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colors.primaryVariant
    ) {
        Column(modifier = Modifier.padding(20.dp, 20.dp, 20.dp, 0.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically) {
                Image(painter = painterResource(R.drawable.si_clipboard_filled),
                    null, Modifier.size(25.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(title),
                    style = MaterialTheme.typography.body2.copy(shadow = Shadow(
                            Color.Black,
                            Offset(3f, 3f),
                            5f
                        )
                    ),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(text = stringResource(desc),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = stringResource(R.string.readDocument),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ButtonLegal(modifier: Modifier){
    val checked = remember { mutableStateOf(false)}
    Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
        verticalAlignment = CenterVertically) {
        Checkbox(checked = checked.value, onCheckedChange = { checked.value = it},
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.background,
                uncheckedColor = MaterialTheme.colors.background,
                checkmarkColor = MaterialTheme.colors.onPrimary,
            )
        )
        Text(text = stringResource(id = R.string.acceptPolicy),
            style = MaterialTheme.typography.body2)
    }
}
