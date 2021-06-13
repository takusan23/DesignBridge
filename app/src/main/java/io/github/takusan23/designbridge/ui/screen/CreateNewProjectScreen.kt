package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R

/**
 * 新規プロジェクト作成画面
 *
 * @param onCreateClick 作成ボタンを押したら呼ばれる
 * */
@Composable
fun CreateNewProjectScreen(onCreateClick: (String) -> Unit) {
    // 入力中テキスト
    val inputText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "新規プロジェクトを作成",
            fontSize = 20.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = inputText.value,
            label = { Text(text = "プロジェクト名") },
            onValueChange = { inputText.value = it },
        )
        // 作成ボタン
        Button(
            modifier = Modifier
                .padding(10.dp),
            onClick = { onCreateClick(inputText.value) }
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_outline_create_new_folder_24), contentDescription = null)
            Text(text = "作成")
        }
    }

}