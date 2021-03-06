package io.github.takusan23.designbridge.ui.screen

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.tool.GetElementValue
import io.github.takusan23.designbridge.tool.HideKeyboard
import io.github.takusan23.designbridge.ui.component.HtmlEditorNavigationBar
import io.github.takusan23.designbridge.ui.component.HtmlWebViewPreview
import io.github.takusan23.designbridge.ui.screen.elementedit.ElementEditScreen
import io.github.takusan23.designbridge.viewmodel.HtmlEditorViewModel
import kotlinx.coroutines.launch

/**
 * HTML編集、プレビュー画面
 * */
@ExperimentalMaterialApi
@Composable
fun HtmlEditorScreen(viewModel: HtmlEditorViewModel) {
    val scope = rememberCoroutineScope()
    // Jetpack Compose Navigation
    val navController = rememberNavController()
    // 今のページ
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    // 編集中要素ID
    val editId = remember { mutableStateOf("") }
    // 編集中テキスト
    val editText = remember { mutableStateOf("") }
    // 編集中タグ
    val editTagName = remember { mutableStateOf("") }
    // 押したときの遷移先。aタグではなくjsでlocationの値を変えてる
    val editLocation = remember { mutableStateOf("") }
    // BottomSheetの状態を覚えるやつ
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    // BottomSheet閉じたらキーボードも閉じる
    if (LocalContext.current is Activity && !sheetState.isVisible) {
        HideKeyboard.hideKeyboard(LocalContext.current as Activity)
    }

    /** ElementEditScreenで編集した値を編集中要素にセットする。 */
    fun setElementContent() {
        viewModel.changeElementTagName(editId.value, editTagName.value)
        viewModel.setClickLocation(editId.value, editLocation.value)
        when (editTagName.value) {
            "img", "video" -> viewModel.setImgOrVideoElementSrc(editId.value, editText.value)
            "span" -> viewModel.setElementText(editId.value, editText.value)
            "input" -> viewModel.setInputElement(editId.value, editText.value)
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            // 編集画面
            Box(modifier = Modifier.heightIn(100.dp)) {
                if (editId.value.isNotEmpty()) {
                    // Html要素編集画面
                    ElementEditScreen(
                        projectName = viewModel.projectName,
                        tagName = editTagName.value,
                        textValue = editText.value,
                        location = editLocation.value,
                        onTextChange = { text ->
                            editText.value = text
                            setElementContent()
                        },
                        onTagNameChange = { tagName ->
                            editTagName.value = tagName
                            setElementContent()
                        },
                        onLocationChange = { location ->
                            editLocation.value = location
                            setElementContent()
                        }
                    )
                }
            }
        },
        content = {
            Scaffold(
                bottomBar = {
                    HtmlEditorNavigationBar(currentRouteName = currentRoute) { route ->
                        scope.launch {
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
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        text = { Text(text = "保存") },
                        icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_save_24), contentDescription = null) },
                        onClick = { scope.launch { viewModel.saveHtml() } }
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
                                    // 編集画面を開く
                                    scope.launch {
                                        editText.value = GetElementValue.getSrcOrTextOrValue(it)
                                        editId.value = it.id()
                                        editTagName.value = it.tagName()
                                        editLocation.value = GetElementValue.getOnClickLocationURL(it)
                                        sheetState.show()
                                    }
                                }
                            )
                        }
                        composable("preview") {
                            // プレビュー画面
                            HtmlWebViewPreview(
                                modifier = Modifier.padding(bottom = 56.dp),
                                url = viewModel.editHtmlFilePath
                            )
                        }
                    }
                }
            )
        }
    )


}



