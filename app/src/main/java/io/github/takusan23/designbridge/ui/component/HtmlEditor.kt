package io.github.takusan23.designbridge.ui.component

import android.net.Uri
import android.util.Base64
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.takusan23.designbridge.R
import org.jsoup.nodes.Element
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


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
    buttonContent: @Composable RowScope.() -> Unit
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
 * @param html HTML本文
 * @param modifier Modifier
 * */
@Composable
fun HtmlWebViewPreview(
    modifier: Modifier = Modifier,
    html: String,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                setWebViewClient(WebViewClient())
                settings.javaScriptEnabled = true
                settings.builtInZoomControls = true
                // loadUrl("takusan.negitoro.dev")
                loadData(Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING), "text/html", "base64")
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
 * @param onElementClick リストのアイテム押したときに呼ばれる
 * */
@ExperimentalMaterialApi
@Composable
fun HtmlElementList(
    elementList: List<Element>,
    onElementClick: (Element) -> Unit,
) {
    LazyColumn {
        items(elementList, key = { it.html() }) { element ->
            HtmlElementListItem(element, onElementClick)
            Divider()
        }
    }
}

/**
 * Elementリストの各アイテム
 * @param element Html要素
 * @param onClick アイテム押したとき
 * */
@ExperimentalMaterialApi
@Composable
private fun HtmlElementListItem(
    element: Element,
    onClick: (Element) -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Row {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Text(text = element.id())
                Text(
                    text = element.text(),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            IconButton(onClick = { onClick(element) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_edit_24),
                    contentDescription = "編集"
                )
            }
        }
    }
}