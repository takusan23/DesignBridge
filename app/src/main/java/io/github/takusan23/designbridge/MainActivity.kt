package io.github.takusan23.designbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.designbridge.ui.screen.*
import io.github.takusan23.designbridge.ui.theme.DesignBridgeTheme
import io.github.takusan23.designbridge.viewmodel.factory.HtmlEditorViewModelFactory
import io.github.takusan23.designbridge.viewmodel.factory.ProjectFolderViewModelFactory

/**
 * Activity一つでComposeを利用して複数の画面を扱う予定
 * */
class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesignBridgeTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "project") {
                        composable("project") {
                            // プロジェクト選択、作成画面
                            ProjectScreen(
                                viewModel = viewModel(),
                                onEditClick = { navController.navigate("project_detail/${it.name}") },
                                onSettingMenuClick = { route -> navController.navigate(route) }
                            )
                        }
                        composable("project_detail/{project_name}") { entry ->
                            val projectName = entry.arguments?.getString("project_name")!!
                            // プロジェクトのフォルダの中身表示画面
                            ProjectDetailScreen(
                                viewModel = viewModel(factory = ProjectFolderViewModelFactory(application, projectName)),
                                onBackClick = { navController.popBackStack() },
                                onEditHtmlClick = { file -> navController.navigate("editor?path=${file.path}") }
                            )
                        }
                        composable("editor?path={path}") { entry ->
                            // Htmlのパラメーターで受け取る。URLの一部で受け取るとファイルパスに妨害される
                            val htmlFilePath = entry.arguments?.getString("path")!!
                            // 編集画面
                            HtmlEditorScreen(viewModel = viewModel(factory = HtmlEditorViewModelFactory(application, htmlFilePath)))
                        }
                        composable("license") {
                            // ライセンス画面
                            LicenseScreen(onBack = { navController.popBackStack() })
                        }
                        composable("konoapp") {
                            // このアプリについて
                            KonoAppScreen()
                        }
                    }
                }
            }
        }
    }

}
