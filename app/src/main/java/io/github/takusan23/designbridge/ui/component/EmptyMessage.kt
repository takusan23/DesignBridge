package io.github.takusan23.designbridge.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R

/**
 * プロジェクトフォルダ内にアイテムが内ときに追加してねメッセージを出すためのやつ
 * */
@Composable
fun EmptyProjectFolderItem() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_outline_upload_file_24), contentDescription = null, modifier = Modifier
            .size(50.dp)
            .padding(5.dp))
        Text(
            text = "プロジェクトの中は空です",
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        )
        Text(
            text = """
                Adobe XDのWeb Exportプラグインを利用して生成したファイルをこの中に追加してください。
                また、動画ファイルも追加しておくと編集時に参照できます。
                ファイルの追加では複数のファイルを一度に選択できるはずです。
            """.trimIndent(),
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Center
        )
    }
}