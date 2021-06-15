package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.runtime.Composable
import io.github.takusan23.designbridge.tool.GetElementSrcOrText
import org.jsoup.nodes.Element

/**
 * HTML要素編集画面
 *
 * @param element 編集する要素
 * */
@Composable
fun ElementEditScreen(element: Element, projectName: String) {
    when (element.tagName()) {
        "img" -> ImgElementEditScreen(
            initValue = GetElementSrcOrText.getSrcOrText(element = element),
            projectName = projectName,
            onSrcValue = { src -> element.attr("src", src) }
        )
        else -> SpanElementEditScreen(
            initValue = GetElementSrcOrText.getSrcOrText(element),
            onTextValue = { text -> element.text(text) }
        )
    }
}