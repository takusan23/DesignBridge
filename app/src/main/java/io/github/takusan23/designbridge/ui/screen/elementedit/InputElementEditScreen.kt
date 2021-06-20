package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.R

/**
 * input要素を編集する画面
 *
 * @param value 初期値
 * @param onValueChange 値が変更されたら呼ばれる
 * */
@Composable
fun InputElementEditScreen(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = value,
            label = { Text(text = "テキストボックスの中身編集") },
            onValueChange = { onValueChange(it) },
            trailingIcon = {
                // クリアボタン
                IconButton(onClick = { onValueChange("") }) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_backspace_24), contentDescription = null)
                }
            }
        )
    }
}