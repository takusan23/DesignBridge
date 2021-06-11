package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.designbridge.ui.component.HtmlEditorNavigationBar
import io.github.takusan23.designbridge.ui.component.HtmlWebViewPreview
import io.github.takusan23.designbridge.ui.component.OpenHtmlFileButton
import io.github.takusan23.designbridge.viewmodel.HtmlEditorViewModel
import kotlinx.coroutines.launch
import org.jsoup.nodes.Element

/**
 * HTML編集、プレビュー画面
 * */
@ExperimentalMaterialApi
@Composable
fun HtmlEditorScreen(viewModel: HtmlEditorViewModel) {

    // Jetpack Compose Navigation
    val navController = rememberNavController()
    // 今のページ
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    /** 編集する要素のID */
    val editElementId = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            ElementEditScreen { text ->
                viewModel.setElementText(editElementId.value, text)
                scope.launch { sheetState.hide() }
            }
        },
        content = {
            Scaffold(
                bottomBar = { HtmlEditorNavigationBar(currentRouteName = currentRoute) { route -> navController.navigate(route) } },
                content = {
                    NavHost(navController = navController, startDestination = "editor") {
                        composable("editor") {
                            EditorScreen(
                                viewModel = viewModel,
                                onElementClick = {
                                    editElementId.value = it.id()
                                    scope.launch { sheetState.show() }
                                }
                            )
                        }
                        composable("preview") {
                            val html = viewModel.htmlLiveData.observeAsState()
                            if (html.value != null) {
                                HtmlWebViewPreview(html = html.value!!)
                            }
                        }
                    }
                }
            )
        }
    )


}



