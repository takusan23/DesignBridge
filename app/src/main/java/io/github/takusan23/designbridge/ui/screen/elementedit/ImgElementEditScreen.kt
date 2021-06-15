package io.github.takusan23.designbridge.ui.screen.elementedit

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.project.ProjectDetail

/**
 * img要素を編集する画面
 *
 * @param initValue 初期値。編集前テキスト的な
 * @param onSrcValue テキストが変更されたら呼ばれる
 * @param
 * */
@Composable
fun ImgElementEditScreen(
    initValue: String,
    projectName: String,
    onSrcValue: (String) -> Unit,
) {
    val srcValue = remember(key1 = initValue) { mutableStateOf(initValue) }

    Column {
        ElementEditComponent(
            title = "画像のリンクを編集",
            hint = "インターネット上の画像も利用できます",
            text = srcValue.value,
            onChangeValue = {
                onSrcValue(it)
                srcValue.value = it
            },
        )
        // その他（プロジェクト内の画像を表示するなど）
        Divider(Modifier.padding(start = 5.dp, end = 5.dp))
        ProjectImgRow(
            projectName = projectName,
            onImageClick = { imgName ->
                srcValue.value = imgName
                onSrcValue(imgName)
            }
        )
    }

}

/**
 * プロジェクト内の画像を一覧表示する
 *
 * @param projectName プロジェクト名
 * */
@Composable
private fun ProjectImgRow(projectName: String, onImageClick: (String) -> Unit) {
    val context = LocalContext.current
    // 画像のBitmapとファイル名のpair
    val imgList = remember {
        ProjectDetail(context, projectName)
            .getProjectImageList()
            .map { img -> Pair(BitmapFactory.decodeFile(img.path), img.name) }
    }

    Text(
        text = "プロジェクト内の画像",
        modifier = Modifier.padding(5.dp)
    )
    LazyRow(modifier = Modifier.padding(5.dp)) {
        items(imgList) { pair ->
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .width(100.dp)
                    .aspectRatio(1.7f)
                    .clickable { onImageClick(pair.second) },
                bitmap = pair.first.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}