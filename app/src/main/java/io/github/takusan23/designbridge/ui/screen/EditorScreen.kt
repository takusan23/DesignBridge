package io.github.takusan23.designbridge.ui.screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import io.github.takusan23.designbridge.viewmodel.HtmlEditorViewModel
import org.jsoup.nodes.Element

/**
 * HTML要素一覧画面
 * @param onEditClick リスト押したら呼ばれる
 * */
@ExperimentalMaterialApi
@Composable
fun EditorScreen(viewModel: HtmlEditorViewModel, onEditClick: (Element) -> Unit) {
    // HTML要素一覧
    val spanElementList = viewModel.htmlSpanElementListLiveData.observeAsState()
    val imgElementList = viewModel.htmlImgElementListLiveData.observeAsState()
    val inputElementList = viewModel.htmlInputElementListLiveData.observeAsState()
    if (spanElementList.value != null && imgElementList.value != null) {
        ElementListScreen(
            spanElementList = spanElementList.value!!,
            imgElementList = imgElementList.value!!,
            inputElementList = inputElementList.value!!,
            allElementList = spanElementList.value!! + imgElementList.value!! + inputElementList.value!!,
            onEditClick = onEditClick
        )
    }
}