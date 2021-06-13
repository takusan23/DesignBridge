package io.github.takusan23.designbridge.ui.screen

import androidx.compose.runtime.Composable
import io.github.takusan23.designbridge.ui.component.ProjectMenu
import io.github.takusan23.designbridge.viewmodel.ProjectDetailViewModel

/**
 * プロジェクトの中身画面で使うメニュー
 *
 * @param viewModel ViewModel
 * @param fileName ファイル名
 * */
@Composable
fun ProjectDetailMenuScreen(viewModel: ProjectDetailViewModel, fileName: String) {
    ProjectMenu(
        fileName = fileName,
        onDeleteClick = { viewModel.deleteFile(fileName) }
    )
}