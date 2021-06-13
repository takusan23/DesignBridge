package io.github.takusan23.designbridge.ui.screen

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.ui.component.ProjectList
import io.github.takusan23.designbridge.ui.component.TitleBar
import io.github.takusan23.designbridge.viewmodel.ProjectListViewModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * プロジェクト管理画面。
 *　
 * プロジェクトはアプリ固有ストレージに保存される。ほんまクソAndroidの仕様変更のせいでストレージアクセスに制限がかかったので。まじで考え直せGoogle
 *
 * @param viewModel プロジェクト一覧ViewModel
 * @param onProjectClick プロジェクトを選択したら呼ばれる
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectScreen(viewModel: ProjectListViewModel, onProjectClick: (File) -> Unit) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    // Sheetを切り替えるNavController
    val navController = rememberNavController()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            CreateNewProjectScreen {
                viewModel.createNewProject(it)
            }
        },
        content = {
            Scaffold(
                topBar = { TitleBar(title = { Text(text = "プロジェクト一覧") }) },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        text = { Text(text = "プロジェクトの作成") },
                        icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_create_new_folder_24), contentDescription = null) },
                        onClick = {
                            // 作成画面を開く
                            // navController.navigate("create")
                            scope.launch { sheetState.show() }
                        }
                    )
                },
                content = {
                    // プロジェクト一覧をFlowで受け取る
                    val projectList = viewModel.projectListFlow.collectAsState(initial = listOf())
                    if (projectList.value.isNotEmpty()) {
                        ProjectList(
                            list = projectList.value,
                            onProjectClick = onProjectClick,
                            onMenuClick = { file ->
                                // メニューを表示
                                // navController.navigate("menu/${file.name}")
                                scope.launch { sheetState.show() }
                            }
                        )
                    }
                }
            )
        }
    )

}