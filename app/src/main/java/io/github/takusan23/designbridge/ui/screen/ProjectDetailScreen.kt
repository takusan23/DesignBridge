package io.github.takusan23.designbridge.ui.screen

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.ProductionActivity
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.ui.component.EmptyProjectFolderItem
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
        uriList.forEach { uri -> viewModel.addFileFromUri(uri) }
    }
    val xdFileCallback = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
        viewModel.importXDFile(uri)
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
                    Column(horizontalAlignment = Alignment.End) {
                        ExtendedFloatingActionButton(
                            modifier = Modifier.padding(2.dp),
                            backgroundColor = MaterialTheme.colors.primary,
                            text = { Text(text = "xdファイルを読み込む（β）") },
                            icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_upload_file_24), contentDescription = null) },
                            onClick = {
                                xdFileCallback.launch(arrayOf("*/*"))
                                Toast.makeText(context, "おまけ程度の変換機能ですので期待しないで。xdファイルをHTMLに変換します", Toast.LENGTH_SHORT).show()
                            }
                        )
                        ExtendedFloatingActionButton(
                            modifier = Modifier.padding(2.dp),
                            backgroundColor = MaterialTheme.colors.primary,
                            text = { Text(text = "ファイルの追加") },
                            icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_upload_file_24), contentDescription = null) },
                            onClick = { safCallback.launch(arrayOf("text/html", "image/*", "video/*")) }
                        )
                    }
                },
                content = {
                    val projectItemList = viewModel.projectFolderItemFlow.collectAsState(initial = listOf())
                    if (projectItemList.value.isEmpty()) {
                        // ファイル追加してメッセージ
                        EmptyProjectFolderItem()
                    } else {
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
                }
            )
        }
    )
}