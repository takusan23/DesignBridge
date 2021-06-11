package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Html要素編集画面
 *
 * @param onTextChange テキストが変更されたら呼ばれる
 * */
@Composable
fun ElementEditScreen(text: String, onEditTextChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "HTMLの要素編集",
            fontSize = 20.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = text,
            label = { Text(text = "テキストの変更") },
            onValueChange = { onEditTextChange(it) }
        )
    }
}