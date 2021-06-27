package io.github.takusan23.designbridge.ui.component

import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.tool.GetElementValue
import org.jsoup.nodes.Element

/**
 * WebViewを利用してHTMLをプレビューする
 * @param url HTMLファイルパス
 * @param modifier Modifier
 * */
@Composable
fun HtmlWebViewPreview(
    modifier: Modifier = Modifier,
    url: String,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                setWebViewClient(WebViewClient())
                setWebContentsDebuggingEnabled(true)
                settings.javaScriptEnabled = true
                settings.builtInZoomControls = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.allowFileAccess = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.displayZoomControls = false
                loadUrl("file://$url")
            }
        }
    )
}

/**
 * HTML編集、プレビュー画面切り替えBottomNavigation
 * @param currentRouteName 今の表示している画面の名前
 * @param onRouteChange BottomNavigationItem押したときに呼ばれる。そのままnavigate()できる
 * */
@Composable
fun HtmlEditorNavigationBar(
    currentRouteName: String?,
    onRouteChange: (String) -> Unit,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_edit_24), contentDescription = null) },
            label = { Text(text = "HTML編集") },
            onClick = { onRouteChange("editor") },
            selected = "editor" == currentRouteName
        )
        BottomNavigationItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_preview_24), contentDescription = null) },
            label = { Text(text = "プレビュー") },
            onClick = { onRouteChange("preview") },
            selected = "preview" == currentRouteName
        )
    }
}

/**
 * HTMLの要素を一覧で表示する
 * @param elementList Htmlの要素の配列
 * @param onEditClick 編集ボタン押したとき
 * */
@ExperimentalMaterialApi
@Composable
fun HtmlElementList(
    elementList: List<Element>,
    onEditClick: (Element) -> Unit,
) {
    LazyColumn {
        // keyが変更されたら再描画される仕組みらしい。あと多分keyが重複するので適当に対策
        items(elementList, key = { GetElementValue.getSrcOrTextOrValue(it) + elementList.indexOf(it) }) {
            HtmlElementListItem(it, onEditClick)
            Divider()
        }
    }
}

/**
 * Elementリストの各アイテム。span版
 * @param element Html要素
 * @param onClick アイテム押したとき
 * */
@ExperimentalMaterialApi
@Composable
private fun HtmlSpanListItem(
    element: Element,
    onClick: (Element) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(element) }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_text_fields_24),
                contentDescription = "span"
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Text(text = element.text())
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_edit_24),
                contentDescription = "編集"
            )
        }
    }
}

/**
 * Elementリストの各アイテム
 *
 * @param element Html要素
 * @param onClick アイテム押したとき
 * */
@ExperimentalMaterialApi
@Composable
private fun HtmlElementListItem(
    element: Element,
    onClick: (Element) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(element) }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = when (element.tagName()) {
                    "input" -> painterResource(id = R.drawable.ic_outline_keyboard_24)
                    "img" -> painterResource(id = R.drawable.ic_outline_photo_size_select_actual_24)
                    "video" -> painterResource(id = R.drawable.ic_outline_local_movies_24)
                    else -> painterResource(id = R.drawable.ic_outline_text_fields_24)
                },
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = when (element.tagName()) {
                        "input" -> element.attr("value")
                        "video", "img" -> element.attr("src")
                        else -> element.text()
                    }
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_edit_24),
                contentDescription = "編集"
            )
        }
    }
}

/**
 * 表示する要素を選ぶボタン
 *
 * @param currentPos 選択位置。0がすべて、1がテキスト、2が画像　のように
 * @param onClick ボタンを押したら呼ばれる
 * */
@Composable
fun ElementFilterChip(
    currentPos: Int,
    onClick: (Int) -> Unit
) {
    val buttonList = listOf("すべて", "テキスト", "画像/動画", "テキストボックス")

    LazyRow {
        itemsIndexed(buttonList) { index, text ->
            // 選択中ならtrue
            val isSelected = currentPos == index
            OutlinedButton(
                modifier = Modifier.padding(5.dp),
                shape = RoundedCornerShape(50),
                border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colors.primary) else ButtonDefaults.outlinedBorder,
                onClick = { onClick(index) },
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isSelected) {
                            // 選択中ならチェックを
                            Icon(
                                modifier = Modifier.height(20.dp),
                                painter = painterResource(id = R.drawable.ic_baseline_check_24),
                                contentDescription = null
                            )
                        }
                        Text(text = text)
                    }
                }
            )
        }
    }
}