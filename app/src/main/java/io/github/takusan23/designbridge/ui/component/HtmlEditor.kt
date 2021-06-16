package io.github.takusan23.designbridge.ui.component

import android.net.Uri
import android.webkit.WebView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.takusan23.designbridge.R
import org.jsoup.nodes.Element
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.Alignment
import io.github.takusan23.designbridge.tool.GetElementSrcOrText


/**
 * HTMLファイルをStorage Access Frameworkから取り出すボタン
 *
 * @param buttonContent ボタンの中身描画するコンポーネント。テキストとか
 * @param modifier Paddingなど
 * @param contentType 取得するファイルのMIME-Type。省略時はhtml
 * @param onResultFileUri ファイルのUriのコールバック
 * */
@Composable
fun OpenHtmlFileButton(
    modifier: Modifier = Modifier,
    onResultFileUri: (Uri) -> Unit,
    contentType: String = "text/html",
    buttonContent: @Composable RowScope.() -> Unit,
) {
    // Activity Result API
    val callback = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
        onResultFileUri(uri)
    }

    Button(
        modifier = modifier,
        onClick = { callback.launch(arrayOf(contentType)) },
        content = buttonContent
    )
}

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
                settings.javaScriptEnabled = true
                settings.builtInZoomControls = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.allowFileAccess = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.displayZoomControls = false
                loadUrl(url)
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
        items(elementList, key = { GetElementSrcOrText.getSrcOrText(it) + elementList.indexOf(it) }) {
            when (it.tagName()) {
                "span" -> {
                    HtmlSpanListItem(it, onEditClick)
                }
                "img", "video" -> {
                    HtmlImgOrVideoListItem(it, onEditClick)
                }
            }
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
 * Elementリストの各アイテム。img版
 * @param element Html要素
 * @param onClick アイテム押したとき
 * */
@ExperimentalMaterialApi
@Composable
private fun HtmlImgOrVideoListItem(
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
                painter = if (element.tagName() == "video") {
                    painterResource(id = R.drawable.ic_outline_local_movies_24)
                } else {
                    painterResource(id = R.drawable.ic_outline_photo_size_select_actual_24)
                },
                contentDescription = "span"
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Text(text = element.attr("src"))
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_edit_24),
                contentDescription = "編集"
            )
        }
    }
}

/**
 * ElementListScreenで使うTabLayout
 * @param selectIndex 選択位置。0から
 * @param onTabClick タブを押したときに呼ばれる
 * */
@Composable
fun ElementListScreenTab(
    selectIndex: Int,
    onTabClick: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectIndex,
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.background,
        indicator = { tabPositions ->
            // テキストの下に出るあの棒のやつ
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectIndex])
                    .height(3.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .background(LocalContentColor.current, RoundedCornerShape(100, 100, 0, 0))
            )
        }
    ) {
        Tab(
            selected = 0 == selectIndex,
            modifier = Modifier.padding(5.dp),
            onClick = { onTabClick(0) },
            content = {
                Icon(painter = painterResource(id = R.drawable.ic_outline_text_fields_24), contentDescription = null)
                Text(text = "文字の変更")
            }
        )
        Tab(
            selected = 1 == selectIndex,
            modifier = Modifier.padding(5.dp),
            onClick = { onTabClick(1) },
            content = {
                Icon(painter = painterResource(id = R.drawable.ic_outline_photo_size_select_actual_24), contentDescription = null)
                Text(text = "画像/動画の変更")
            }
        )
    }
}