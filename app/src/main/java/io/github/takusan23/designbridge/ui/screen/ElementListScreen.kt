package io.github.takusan23.designbridge.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.github.takusan23.designbridge.ui.component.ElementListScreenTab
import io.github.takusan23.designbridge.ui.component.HtmlElementList
import org.jsoup.nodes.Element

/**
 * HTMLの要素一覧表示画面
 *
 * @param imgElementList imgタグ一覧
 * @param spanElementList spanタグ一覧
 * @param onEditClick 編集押したときに呼ばれる
 * */
@ExperimentalMaterialApi
@Composable
fun ElementListScreen(
    spanElementList: List<Element>,
    imgElementList: List<Element>,
    onEditClick: (Element) -> Unit,
) {
    // タブ選択位置
    val currentTabPos = remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            ElementListScreenTab(selectIndex = currentTabPos.value) { currentTabPos.value = it }
        },
        content = {
            // HTML要素一覧表示
            when (currentTabPos.value) {
                0 -> HtmlElementList(elementList = spanElementList, onEditClick = onEditClick)
                1 -> HtmlElementList(elementList = imgElementList, onEditClick = onEditClick)
            }
        }
    )

}