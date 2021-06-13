package io.github.takusan23.designbridge.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.R
import java.io.File

/**
 * プロジェクト一覧
 *
 * @param list プロジェクトのフォルダ配列
 * @param onProjectClick プロジェクトを選択したとき
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectList(
    list: List<File>,
    onProjectClick: (File) -> Unit,
    onMenuClick: (File) -> Unit,
) {
    LazyColumn {
        items(list) { file ->
            ProjectListItem(file, onProjectClick, onMenuClick)
            Divider()
        }
    }
}

/**
 * プロジェクト一覧の各アイテム
 *
 * @param file プロジェクトのフォルダ
 * @param onProjectClick プロジェクトを選択したとき
 * */
@ExperimentalMaterialApi
@Composable
private fun ProjectListItem(
    file: File,
    onProjectClick: (File) -> Unit,
    onMenuClick: (File) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onProjectClick(file) }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_outline_photo_size_select_actual_24),
                contentDescription = "span"
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Text(text = file.name)
            }
            IconButton(onClick = { onMenuClick(file) }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_more_vert_24), contentDescription = null)
            }
        }
    }
}