package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.R

/**
 * Span要素編集画面
 *
 * @param onTextValue テキストが変更されたら呼ばれる
 * @param value 編集中テキスト
 * @param projectName プロジェクト名
 * @param location 押したときの遷移先
 * @param onLocationChange 遷移先が変わったら呼ばれる
 * */
@Composable
fun SpanElementEditScreen(
    value: String,
    location: String,
    projectName: String,
    onTextValue: (String) -> Unit,
    onLocationChange: (String) -> Unit,
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = value,
            label = { Text(text = "テキストの変更") },
            onValueChange = { onTextValue(it) },
            trailingIcon = {
                // クリアボタン
                IconButton(onClick = { onTextValue("") }) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_backspace_24), contentDescription = null)
                }
            }
        )
        // 押したときの遷移先ページ
        ElementEditLocation(
            projectName = projectName,
            location = location,
            onLocationChange = onLocationChange
        )
    }
}