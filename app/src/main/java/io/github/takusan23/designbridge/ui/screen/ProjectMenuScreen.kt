package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.ui.component.FillTextButton
import io.github.takusan23.designbridge.viewmodel.ProjectListViewModel

/**
 * メニュー。削除とか
 *
 * @param viewModel プロジェクト一覧ViewModel
 * */
@Composable
fun ProjectMenuScreen(viewModel: ProjectListViewModel, projectName: String) {
    Column {
        FillTextButton(
            modifier = Modifier.padding(10.dp),
            onClick = { viewModel.deleteProject(projectName) },
            content = {
                Icon(painter = painterResource(id = R.drawable.ic_outline_delete_24), contentDescription = null)
                Text(text = "プロジェクトを削除する")
            }
        )
    }
}