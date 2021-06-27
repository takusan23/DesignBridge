package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.project.ProjectDetail

/**
 * 押したときの遷移先を変更する画面
 * @param projectName プロジェクト名
 * @param location 押したときの遷移先
 * @param onLocationChange 遷移先が変わったら呼ばれる
 * */
@Composable
fun ElementEditLocation(
    projectName: String,
    location: String,
    onLocationChange: (String) -> Unit,
) {
    // 押したときの遷移先ページ
    Divider(Modifier.padding(start = 5.dp, end = 5.dp))
    OutlinedTextField(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        value = location,
        label = { Text(text = "押したときの移動先。好きなサイトのURLでもいけます") },
        onValueChange = {
            onLocationChange(it)
        },
        trailingIcon = {
            // クリアボタン
            IconButton(onClick = { onLocationChange("") }) {
                Icon(painter = painterResource(id = R.drawable.ic_outline_backspace_24), contentDescription = null)
            }
        }
    )
    ProjectHtmlRow(
        projectName = projectName,
        onHtmlClick = { onLocationChange(it) }
    )
}

/**
 * プロジェクト内のhtmlファイルを横並びで表示する
 *
 * @param projectName プロジェクト名
 * @param onHtmlClick 相対パスを返します
 * */
@Composable
private fun ProjectHtmlRow(projectName: String, onHtmlClick: (String) -> Unit) {
    val context = LocalContext.current
    // htmlファイル一覧
    val htmlList = remember { ProjectDetail(context, projectName).getProjectHtmlList() }

    Text(
        text = "プロジェクト内の画面一覧 (HTML)",
        modifier = Modifier.padding(5.dp),
        fontSize = 18.sp,
    )
    LazyRow(modifier = Modifier.padding(5.dp)) {
        items(htmlList) { htmlFile ->
            OutlinedButton(
                modifier = Modifier.padding(start = 2.dp, end = 2.dp),
                onClick = { onHtmlClick(htmlFile.name) },
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_language_24),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = htmlFile.name,
                    fontSize = 12.sp
                )
            }
        }
    }
}