package com.example.ofiu.usecases.users.workerUser.verifyId

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ofiu.R

@Composable
fun VerifyWorkerApp(){
    VerifyContent()
}

@Composable
fun VerifyContent(){
    Column() {
        Text(text = stringResource(id = R.string.policy),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.secondaryVariant
        )
    }

}
