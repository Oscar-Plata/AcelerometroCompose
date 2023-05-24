package com.argent.acelerometrocompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ModoScreen(onHome: () -> Unit, onSolo: () -> Unit, onDuo: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.ModoDeApp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Button(onClick = onSolo ) {
            Text(text = "Solo")
        }
        Button(onClick = onDuo ) {
            Text(text = "Duo")
        }
    }
}