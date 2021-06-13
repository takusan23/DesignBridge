package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import io.github.takusan23.designbridge.ui.component.FillRow
import io.github.takusan23.designbridge.ui.component.OpenHtmlFileButton
import io.github.takusan23.designbridge.viewmodel.HtmlEditorViewModel
import org.jsoup.nodes.Element

/**
 * HTML編集画面。
 * */
@ExperimentalMaterialApi
@Composable
fun EditorScreen(viewModel: HtmlEditorViewModel, onEditClick: (Element) -> Unit) {
    val htmlLiveData = viewModel.htmlLiveData.observeAsState()

    if (htmlLiveData.value == null) {
        // まだHTML設定してない場合はファイルを受け取る
        FillRow {
            OpenHtmlFileButton(onResultFileUri = { uri -> viewModel.setHtmlFromUri(uri) }) {
                Text(text = "HTMLファイル選択")
            }
        }
    } else {
        Column {
            // HTML要素一覧
            val spanElementList = viewModel.htmlSpanElementListLiveData.observeAsState()
            val imgElementList = viewModel.htmlImgElementListLiveData.observeAsState()
            if (spanElementList.value != null && imgElementList.value != null) {
                ElementListScreen(
                    spanElementList = spanElementList.value!!,
                    imgElementList = imgElementList.value!!,
                    onEditClick = onEditClick
                )
            }
        }
    }
}