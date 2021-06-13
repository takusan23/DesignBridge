package io.github.takusan23.designbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.takusan23.designbridge.ui.screen.HtmlEditorScreen
import io.github.takusan23.designbridge.ui.screen.ProjectScreen
import io.github.takusan23.designbridge.ui.theme.DesignBridgeTheme

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
                            ProjectScreen(viewModel = viewModel()) {

                            }
                        }
                        composable("editor") {
                            // 編集画面
                            HtmlEditorScreen(viewModel = viewModel())
                        }
                    }
                }
            }
        }
    }

}
