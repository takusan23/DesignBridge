package io.github.takusan23.designbridge.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R

/**
 * ファイル削除とかのメニュー
 * */
@Composable
fun ProjectMenu(
    fileName: String,
    fileDate: String? = null,
    onDeleteClick: () -> Unit,
) {
    Column {
        ProjectMenuScreenHeader(fileName, fileDate)
        Divider()
        FillTextButton(
            modifier = Modifier.padding(10.dp),
            onClick = { onDeleteClick() },
            content = {
                Icon(painter = painterResource(id = R.drawable.ic_outline_delete_24), contentDescription = null)
                Text(text = "削除する")
            }
        )
    }
}

/**
 * プロジェクト名とかを表示するやつ
 *
 * @param fileName ファイル名
 * @param fileDate ファイルの最終編集日時？
 * */
@Composable
private fun ProjectMenuScreenHeader(fileName: String, fileDate: String?) {
    Surface(modifier = Modifier.fillMaxWidth()) {
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
                Text(text = fileName)
                if (fileDate != null) {
                    Text(text = fileDate, fontSize = 12.sp)
                }
            }
        }
    }
}