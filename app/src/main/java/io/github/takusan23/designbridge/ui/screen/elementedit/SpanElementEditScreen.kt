package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Span要素編集画面
 *
 * @param onTextValue テキストが変更されたら呼ばれる
 * @param initValue 初期値。編集前テキスト
 * */
@Composable
fun SpanElementEditScreen(
    initValue: String,
    onTextValue: (String) -> Unit,
) {
    // keyを指定しないとテキストが変わらない。値が変わってない判定を食らう
    val text = remember(key1 = initValue) { mutableStateOf(initValue) }

    ElementEditComponent(
        title = "テキストの編集",
        text = text.value,
        onChangeValue = {
            onTextValue(it)
            text.value = it
        },
    )
}