package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
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
import io.github.takusan23.designbridge.ui.component.TitleBarDropDown
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
 * @param onSettingMenuClick 右上の設定押したときに出てくるドロップダウンメニューの項目を押したときに呼ばれる。引数はそのままnavigate()へ
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectScreen(
    viewModel: ProjectListViewModel,
    onEditClick: (File) -> Unit,
    onSettingMenuClick: (String) -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    // Sheetを切り替えるNavController
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.id

    /** BottomSheetを表示 */
    fun showSheet() {
        scope.launch { sheetState.show() }
    }

    /** BottomSheetを非表示 */
    fun hideSheet() {
        scope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            // sheetContentの高さが0だと例外スローなので、最低限1dpは確保して高さがあるようにする
            Box(modifier = Modifier.heightIn(1.dp)) {
                NavHost(navController = navController, startDestination = "create") {
                    composable("create") {
                        // 新規作成画面
                        CreateNewProjectScreen { projectName ->
                            viewModel.createNewProject(projectName)
                            hideSheet()
                        }
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
                topBar = {
                    TitleBar(
                        title = { Text(text = "プロジェクト一覧") },
                        actions = {
                            // 設定押したときのメニューを展開するか
                            val isShowMenu = remember { mutableStateOf(false) }
                            // メニューの中身
                            val menuMap = mapOf(
                                "ライセンス" to "license",
                                "このアプリについて" to "konoapp"
                            )
                            IconButton(onClick = { isShowMenu.value = !isShowMenu.value }) { Icon(painter = painterResource(id = R.drawable.ic_outline_settings_24), contentDescription = null) }
                            // ドロップダウンメニュー
                            TitleBarDropDown(
                                isShowMenu = isShowMenu.value,
                                menuMap = menuMap,
                                onMenuClick = { onSettingMenuClick(it) },
                                onShow = { isShowMenu.value = false }
                            )
                        }
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        text = { Text(text = "プロジェクトの作成") },
                        icon = { Icon(painter = painterResource(id = R.drawable.ic_outline_create_new_folder_24), contentDescription = null) },
                        onClick = {
                            // 作成画面を開く
                            navController.navigate("create")
                            showSheet()
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
                            navController.navigate("menu/${file.name}")
                            showSheet()
                        }
                    )
                }
            )
        }
    )

}