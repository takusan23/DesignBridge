package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Span要素編集画面
 *
 * @param onTextValue テキストが変更されたら呼ばれる
 * @param value 編集中テキスト
 * */
@Composable
fun SpanElementEditScreen(
    value: String,
    onTextValue: (String) -> Unit,
) {
    ElementEditComponent(
        title = "テキストの編集",
        text = value,
        onChangeValue = {
            onTextValue(it)
        },
    )
}