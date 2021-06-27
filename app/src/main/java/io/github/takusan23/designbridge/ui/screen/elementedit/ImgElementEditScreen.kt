package io.github.takusan23.designbridge.ui.screen.elementedit

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.project.ProjectDetail
import org.jsoup.nodes.Attributes

/**
 * img/video要素を編集する画面
 *
 * @param src 編集中テキスト
 * @param onSrcValue テキストが変更されたら呼ばれる
 * @param projectName プロジェクト名
 * @param location 押したときの遷移先
 * @param onLocationChange 遷移先が変わったら呼ばれる
 * */
@Composable
fun ImgElementEditScreen(
    src: String,
    projectName: String,
    location: String,
    onSrcValue: (String) -> Unit,
    onLocationChange: (String) -> Unit,
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            value = src,
            label = { Text(text = "画像、動画のパス。インターネット上の画像/動画のURLも利用できます") },
            onValueChange = { onSrcValue(it) },
            trailingIcon = {
                // クリアボタン
                IconButton(onClick = { onSrcValue("") }) {
                    Icon(painter = painterResource(id = R.drawable.ic_outline_backspace_24), contentDescription = null)
                }
            }
        )
        // その他（プロジェクト内の画像を表示するなど）
        Divider(Modifier.padding(start = 5.dp, end = 5.dp))
        ProjectImgOrVideoRow(
            projectName = projectName,
            onImageOrVideoClick = onSrcValue
        )
        // 押したときの遷移先ページ
        ElementEditLocation(
            projectName = projectName,
            location = location,
            onLocationChange = onLocationChange
        )
    }
}

/**
 * プロジェクト内の画像を一覧表示する
 *
 * @param projectName プロジェクト名
 * @param onImageOrVideoClick 画像、動画を押したとき
 * */
@Composable
private fun ProjectImgOrVideoRow(projectName: String, onImageOrVideoClick: (String) -> Unit) {
    val context = LocalContext.current
    // 画像のBitmapとファイル名のpair
    val imgList = remember {
        val projectDetail = ProjectDetail(context, projectName)
        val imgList = projectDetail.getProjectImageList()
        val videoList = projectDetail.getProjectVideoList()
        // 配列足す
        (imgList + videoList).map { imgOrVideo ->
            val bitmap = if (imgOrVideo.extension == "mp4") {
                // 動画のサムネ取得
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ThumbnailUtils.createVideoThumbnail(imgOrVideo, android.util.Size(320, 240), null)
                } else {
                    ThumbnailUtils.createVideoThumbnail(imgOrVideo.path, MediaStore.Images.Thumbnails.MINI_KIND)!!
                }
            } else {
                // 画像取得
                BitmapFactory.decodeFile(imgOrVideo.path)
            }
            Pair(bitmap, imgOrVideo.name)
        }
    }

    Text(
        text = "プロジェクト内の画像/動画",
        modifier = Modifier.padding(5.dp),
        fontSize = 18.sp,
    )
    LazyRow(modifier = Modifier.padding(5.dp)) {
        items(imgList) { pair ->
            Box {
                Image(
                    modifier = Modifier
                        .padding(5.dp)
                        .width(100.dp)
                        .aspectRatio(1.7f)
                        .clickable { onImageOrVideoClick(pair.second) },
                    bitmap = pair.first.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Icon(
                    painter = if (pair.second.split(".").lastOrNull() == "mp4") {
                        painterResource(id = R.drawable.ic_outline_local_movies_24)
                    } else {
                        painterResource(id = R.drawable.ic_outline_photo_size_select_actual_24)
                    },
                    contentDescription = "span",
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}
