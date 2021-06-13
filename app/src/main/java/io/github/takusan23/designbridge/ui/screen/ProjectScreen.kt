package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
 * @param onEditClick 編集ボタンを押したとき
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectScreen(
    viewModel: ProjectListViewModel,
    onEditClick: (File) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    // Sheetを切り替えるNavController
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.id
    // currentRouteの値（ページ遷移先の名前）が変わったらBottomSheetを表示させる
    LaunchedEffect(currentRoute) {
        sheetState.show()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            // sheetContentの高さが0だと例外スローなので、最低限1dpは確保して高さがあるようにする
            Box(modifier = Modifier.heightIn(1.dp)) {
                NavHost(navController = navController, startDestination = "create") {
                    composable("create") {
                        // 新規作成画面
                        CreateNewProjectScreen { projectName -> viewModel.createNewProject(projectName) }
                    }
                    composable("menu/{project_name}") { entry ->
                        val projectName = entry.arguments?.getString("project_name")!!
                        // メニュー画面
                        ProjectMenuScreen(viewModel = viewModel, projectName = projectName)
                    }
                }
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
                            scope.launch { navController.navigate("create") }
                        }
                    )
                },
                content = {
                    // プロジェクト一覧をFlowで受け取る
                    val projectList = viewModel.projectListFlow.collectAsState(initial = listOf())
                    ProjectList(
                        list = projectList.value,
                        onEditClick = onEditClick,
                        onMenuClick = { file ->
                            // メニューを表示
                            scope.launch { navController.navigate("menu/${file.name}") }
                        }
                    )
                }
            )
        }
    )

}