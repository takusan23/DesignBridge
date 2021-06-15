package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.takusan23.designbridge.tool.GetElementSrcOrText
import org.jsoup.nodes.Element

/**
 * HTML要素編集画面
 *
 * @param textValue 入力中テキスト
 * @param tagName タグ名
 * @param projectName プロジェクト名
 * @param onTextChange テキスト変更したら呼ばれる
 * */
@Composable
fun ElementEditScreen(
    textValue: String,
    tagName: String,
    projectName: String,
    onTextChange: (String, String) -> Unit,
) {

    when (tagName) {
        "img", "video" -> ImgElementEditScreen(
            value = textValue,
            projectName = projectName,
            onSrcValue = { src ->
                onTextChange(tagName, src)
            }
        )
        else -> SpanElementEditScreen(
            value = textValue,
            onTextValue = { text ->
                onTextChange(tagName, text)
            }
        )
    }
}