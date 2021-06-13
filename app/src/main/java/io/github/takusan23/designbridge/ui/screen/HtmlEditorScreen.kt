package io.github.takusan23.designbridge.ui.screen

import android.app.Activity
import android.os.Build
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.tool.GetElementSrcOrText
import io.github.takusan23.designbridge.tool.HideKeyboard
import io.github.takusan23.designbridge.ui.component.HtmlEditorNavigationBar
import io.github.takusan23.designbridge.ui.component.HtmlWebViewPreview
import io.github.takusan23.designbridge.viewmodel.HtmlEditorViewModel
import kotlinx.coroutines.launch

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
    val editElementTagName = remember { mutableStateOf("") }
    val editElementText = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // BottomSheet閉じたらキーボードも閉じる
    if (LocalContext.current is Activity && !sheetState.isVisible) {
        HideKeyboard.hideKeyboard(LocalContext.current as Activity)
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            // 編集画面
            ElementEditScreen(
                text = editElementText.value,
                onEditTextChange = { inputValue ->
                    editElementText.value = inputValue
                    when (editElementTagName.value) {
                        "span" -> viewModel.setElementText(editElementId.value, inputValue)
                        "img" -> viewModel.setImgElementSrc(editElementId.value, inputValue)
                    }
                }
            )
        },
        content = {
            Scaffold(
                bottomBar = {
                    HtmlEditorNavigationBar(currentRouteName = currentRoute) { route ->
                        when (route) {
                            "preview" -> {
                                // プレビュー時はHTMLを保存する
                                viewModel.saveHtml()
                                navController.navigate("preview") {
                                    popUpTo("editor") { inclusive = true }
                                }
                            }
                            "editor" -> navController.navigate("editor") {
                                popUpTo("preview") { inclusive = true }
                            }
                        }
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        text = { Text(text = "保存") },
                        icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_save_24), contentDescription = null) },
                        onClick = { viewModel.saveHtml() }
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                content = {
                    NavHost(navController = navController, startDestination = "editor") {
                        composable("editor") {
                            // 編集画面
                            EditorScreen(
                                viewModel = viewModel,
                                onEditClick = {
                                    editElementId.value = it.id()
                                    editElementText.value = GetElementSrcOrText.getSrcOrText(it)
                                    editElementTagName.value = it.tagName()
                                    scope.launch { sheetState.show() }
                                }
                            )
                        }
                        composable("preview") {
                            // プレビュー画面
                            HtmlWebViewPreview(url = viewModel.editHtmlFilePath)
                        }
                    }
                }
            )
        }
    )


}



