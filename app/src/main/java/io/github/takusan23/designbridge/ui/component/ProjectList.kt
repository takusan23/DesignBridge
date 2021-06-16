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
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.tool.FileTool
import io.github.takusan23.designbridge.tool.TimeFormatter
import java.io.File

/**
 * プロジェクト一覧
 *
 * @param list プロジェクトのフォルダ配列
 * @param onEditClick 編集ボタンを押したとき
 * @param onMenuClick メニューボタン押したとき
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectList(
    list: List<File>,
    onEditClick: (File) -> Unit,
    onMenuClick: (File) -> Unit,
) {
    LazyColumn {
        items(list) { file ->
            ProjectListItem(file, onEditClick, onMenuClick)
            Divider()
        }
    }
}

/**
 * プロジェクト一覧の各アイテム
 *
 * @param file プロジェクトのフォルダ
 * @param onShowClick 画面表示ボタンを押したとき
 * @param onEditClick 編集ボタンを押したとき
 * @param onMenuClick メニューボタン押したとき
 * */
@ExperimentalMaterialApi
@Composable
private fun ProjectListItem(
    file: File,
    onEditClick: (File) -> Unit,
    onMenuClick: (File) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxWidth(), onClick = { onEditClick(file) }) {
        Column {
            Row(
                modifier = Modifier
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_folder_24),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                ) {
                    Text(text = file.name)
                    Text(
                        text = TimeFormatter.unixTimeMsToFormatText(file.lastModified()),
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = { onMenuClick(file) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_more_vert_24), contentDescription = null)
                }
            }
        }
    }
}

/**
 * プロジェクト内ファイル一覧表示
 * @param list ファイル一覧
 * @param onShowClick 画面表示押したとき。なおHTML以外では表示しません。
 * @param onEditClick 編集押したとき。なおHTML以外では表示しません。
 * @param onMenuClick メニュー押したとき
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectFolderList(
    list: List<File>,
    onShowClick: (File) -> Unit,
    onEditClick: (File) -> Unit,
    onMenuClick: (File) -> Unit,
) {
    LazyColumn {
        items(list) { file ->
            ProjectFolderListItem(
                file = file,
                onShowClick = onShowClick,
                onEditClick = onEditClick,
                onMenuClick = onMenuClick
            )
            Divider()
        }
    }
}

/**
 * プロジェクト内ファイル一覧表示の各アイテム
 * @param file ファイル
 * @param onShowClick 画面表示押したとき。なおHTML以外では表示しません。
 * @param onEditClick 編集押したとき。なおHTML以外では表示しません。
 * @param onMenuClick メニュー押したとき
 * */
@ExperimentalMaterialApi
@Composable
fun ProjectFolderListItem(
    file: File,
    onShowClick: (File) -> Unit,
    onEditClick: (File) -> Unit,
    onMenuClick: (File) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 拡張子から適切なアイコンを設定する
                val painter = when (file.extension) {
                    "html" -> painterResource(id = R.drawable.ic_baseline_language_24)
                    "png", "jpg" -> painterResource(id = R.drawable.ic_outline_photo_size_select_actual_24)
                    "mp4" -> painterResource(id = R.drawable.ic_outline_local_movies_24)
                    else -> painterResource(id = R.drawable.ic_outline_insert_drive_file_24)
                }
                Icon(
                    painter = painter,
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                        .weight(1f)
                ) {
                    Text(text = file.name)
                    Text(text = "${FileTool.byteToMB(file.length())} MB", fontSize = 12.sp)
                    Text(text = TimeFormatter.unixTimeMsToFormatText(file.lastModified()), fontSize = 12.sp)
                }
                IconButton(onClick = { onMenuClick(file) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_more_vert_24), contentDescription = null)
                }
            }
            // HTML編集、HTML表示ボタン
            if (file.extension == "html") {
                Row(
                    modifier = Modifier
                        .padding(5.dp),
                ) {
                    TextButton(onClick = { onShowClick(file) }, modifier = Modifier.weight(1f)) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_launch_24), contentDescription = null)
                        Text(text = "画面表示(本番環境)")
                    }
                    TextButton(onClick = { onEditClick(file) }, modifier = Modifier.weight(1f)) {
                        Icon(painter = painterResource(id = R.drawable.ic_outline_edit_24), contentDescription = null)
                        Text(text = "テキスト、画像編集")
                    }
                }
            }
        }
    }
}