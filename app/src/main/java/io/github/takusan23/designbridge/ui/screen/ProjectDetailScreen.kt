package io.github.takusan23.designbridge.ui.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.ProductionActivity
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.ui.component.ProjectFolderList
import io.github.takusan23.designbridge.ui.component.TitleBar
import io.github.takusan23.designbridge.viewmodel.ProjectDetailViewModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * プロジェクトの中身を表示する画面
 *
 * @param onBackClick 右上の戻る押したときに呼ばれる
 * @param onEditHtmlClick 編集ボタンを押したときに呼ばれる。HTMLファイルのみ表示される
 * @param viewModel ViewModel
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel,
    onBackClick: () -> Unit,
    onEditHtmlClick: (File) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    // ActivityResultAPIがComposeでも使える
    val safCallback = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenMultipleDocuments()) { uriList ->
        // プロジェクトに追加
        uriList.forEach { uri ->
            viewModel.addFileFromUri(uri)
        }
    }

    // SheetContent切り替え用
    val currentMenuFileName = remember { mutableStateOf<String?>(null) }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            // メニュー
            Box(modifier = Modifier.heightIn(1.dp)) {
                if (currentMenuFileName.value != null) {
                    ProjectDetailMenuScreen(viewModel = viewModel, fileName = currentMenuFileName.value!!)
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    // タイトル
                    TitleBar(
                        icon = { IconButton(onClick = { onBackClick() }) { Icon(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24), contentDescription = null) } },
                        title = { Text(text = viewModel.projectName) }
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        text = { Text(text = "ファイルの追加") },
                        icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_upload_file_24), contentDescription = null) },
                        onClick = { safCallback.launch(arrayOf("text/html", "image/*", "video/*")) }
                    )
                },
                content = {
                    val projectItemList = viewModel.projectFolderItemFlow.collectAsState(initial = listOf())
                    ProjectFolderList(
                        list = projectItemList.value,
                        onEditClick = onEditHtmlClick,
                        onShowClick = {
                            // 本番モード
                            Intent(context, ProductionActivity::class.java).apply {
                                putExtra("file_path", it.path)
                                context.startActivity(this)
                            }
                        },
                        onMenuClick = {
                            currentMenuFileName.value = it.name
                            scope.launch { sheetState.show() }
                        }
                    )
                }
            )
        }
    )
}