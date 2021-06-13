package io.github.takusan23.designbridge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import io.github.takusan23.designbridge.ui.screen.HtmlEditorScreen
import io.github.takusan23.designbridge.ui.screen.ProjectDetailScreen
import io.github.takusan23.designbridge.ui.screen.ProjectScreen
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
                            HtmlEditorScreen(viewModel = viewModel(factory = HtmlEditorViewModelFactory(application,htmlFilePath)))
                        }
                    }
                }
            }
        }
    }

}
