package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun ElementEditScreen(onEditClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val editText = remember { mutableStateOf("") }

        Text(
            modifier = Modifier.padding(10.dp),
            text = "HTMLの要素編集",
            fontSize = 20.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = editText.value,
            label = { Text(text = "テキストの変更") },
            onValueChange = { editText.value = it }
        )
        Button(
            onClick = {
                onEditClick(editText.value)
                editText.value = ""
            },
            Modifier
                .align(Alignment.End)
                .padding(10.dp)
        ) {
            Text(text = "編集完了")
        }
    }
}