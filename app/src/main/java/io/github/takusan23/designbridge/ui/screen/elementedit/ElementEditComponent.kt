package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R

/**
 * HTML要素編集画面。共通部分。ステートレス
 *
 * @param title タイトル。テキスト編集とか
 * @param hint テキストボックスに表示するテキスト
 * @param text 編集中テキスト
 * @param onChangeValue 値が変更されたら呼ばれる
 * */
@Composable
fun ElementEditComponent(
    title: String,
    hint: String = title,
    text: String,
    onChangeValue: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = title,
            fontSize = 20.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = text,
            label = { Text(text = hint) },
            onValueChange = { onChangeValue(it) },
            trailingIcon = {
                // クリアボタン
                IconButton(onClick = { onChangeValue("") }) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_backspace_24), contentDescription = null)
                }
            }
        )
    }

}