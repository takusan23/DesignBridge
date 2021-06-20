package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.ui.component.ElementFilterChip
import io.github.takusan23.designbridge.ui.component.HtmlElementList
import org.jsoup.nodes.Element

/**
 * HTMLの要素一覧表示画面
 *
 * @param imgElementList imgタグ一覧
 * @param spanElementList spanタグ一覧
 * @param inputElementList inputタグ一覧
 * @param allElementList 上記の配列すべてをつなげたもの
 * @param onEditClick 編集押したときに呼ばれる
 * */
@ExperimentalMaterialApi
@Composable
fun ElementListScreen(
    spanElementList: List<Element>,
    imgElementList: List<Element>,
    inputElementList: List<Element>,
    allElementList: List<Element>,
    onEditClick: (Element) -> Unit,
) {
    // タブ選択位置
    val currentTabPos = remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            // フィルター
            ElementFilterChip(
                currentPos = currentTabPos.value,
                onClick = { currentTabPos.value = it }
            )
        },
        content = {
            // BottomNavBarの分引いておく
            Box(modifier = Modifier.padding(bottom = 56.dp)) {
                // HTML要素一覧表示
                HtmlElementList(
                    elementList = when (currentTabPos.value) {
                        1 -> spanElementList
                        2 -> imgElementList
                        3 -> inputElementList
                        else -> allElementList
                    },
                    onEditClick = onEditClick
                )
            }
        }
    )

}