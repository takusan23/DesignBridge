package io.github.takusan23.designbridge.ui.screen

import androidx.compose.runtime.Composable
import io.github.takusan23.designbridge.tool.TimeFormatter
import io.github.takusan23.designbridge.ui.component.ProjectMenu
import io.github.takusan23.designbridge.viewmodel.ProjectListViewModel

/**
 * プロジェクト一覧で使うメニュー。削除とか
 *
 * @param viewModel プロジェクト一覧ViewModel
 * */
@Composable
fun ProjectMenuScreen(viewModel: ProjectListViewModel, projectName: String) {
    val file = viewModel.getProjectFolder(projectName)
    val fileName = file.name
    val fileLastModified = TimeFormatter.unixTimeMsToFormatText(file.lastModified())

    // ステートレスなメニュー
    ProjectMenu(
        fileName = fileName,
        fileDate = fileLastModified,
        onDeleteClick = { viewModel.deleteProject(projectName) }
    )
}