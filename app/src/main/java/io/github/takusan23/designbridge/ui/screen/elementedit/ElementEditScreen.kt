package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * HTML要素編集画面
 *
 * @param textValue 入力中テキスト
 * @param tagName 入力中タグ名
 * @param projectName プロジェクト名
 * @param onTagNameChange タグの名前が変わったら呼ばれる
 * @param onTextChange テキスト変更したら呼ばれる
 * @param location 押したときの遷移先
 * @param onLocationChange 遷移先が変わったら呼ばれる
 * */
@Composable
fun ElementEditScreen(
    textValue: String,
    tagName: String,
    projectName: String,
    location: String,
    onTagNameChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "編集画面",
            fontSize = 20.sp
        )
        // ドロップダウンメニュー
        ElementDropDownMenu(
            currentTagName = tagName,
            onTagSelect = { tagName -> onTagNameChange(tagName) }
        )
        // それぞれのメニュー
        when (tagName) {
            "span" -> SpanElementEditScreen(
                value = textValue,
                projectName = projectName,
                location = location,
                onTextValue = onTextChange,
                onLocationChange = onLocationChange
            )
            "img", "video" -> ImgElementEditScreen(
                src = textValue,
                projectName = projectName,
                location = location,
                onSrcValue = onTextChange,
                onLocationChange = onLocationChange
            )
            "input" -> InputElementEditScreen(
                value = textValue,
                onValueChange = onTextChange
            )
        }
    }
}