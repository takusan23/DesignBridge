package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.*
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
import io.github.takusan23.designbridge.data.EditElementData
import io.github.takusan23.designbridge.tool.GetElementSrcOrText
import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Element
import org.w3c.dom.Attr

/**
 * HTML要素編集画面
 *
 * @param textValue 入力中テキスト
 * @param tagName 入力中タグ名
 * @param projectName プロジェクト名
 * @param attribute 要素のについた属性。srcなど
 * @param onTextChange テキスト変更したら呼ばれる
 * */
@Composable
fun ElementEditScreen(
    textValue: String,
    tagName: String,
    projectName: String,
    onTagNameChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
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
                onTextValue = { text -> onTextChange(text) }
            )
            "img", "video" -> ImgElementEditScreen(
                src = textValue,
                projectName = projectName,
                onSrcValue = { src -> onTextChange(src) }
            )
            "input" -> InputElementEditScreen(
                value = textValue,
                onValueChange = { value -> onTextChange(value) }
            )
        }
    }
}