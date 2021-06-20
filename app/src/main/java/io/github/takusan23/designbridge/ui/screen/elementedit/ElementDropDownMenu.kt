package io.github.takusan23.designbridge.ui.screen.elementedit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.takusan23.designbridge.R

/**
 * タグ名変更ドロップダウン
 *
 * @param onTagSelect タグ名が変更になったら呼ばれる
 * @param currentTagName 選択中のタグ名
 * */
@Composable
fun ElementDropDownMenu(currentTagName: String, onTagSelect: (String) -> Unit) {
    // 種類
    val typeList = listOf("テキスト表示", "画像の表示", "動画の表示", "テキストボックスの表示")
    val tagList = listOf("span", "img", "video", "input")
    val isShow = remember { mutableStateOf(false) }

    /** タグ名から何を表示する部品かを返す */
    fun getTypeFromTagName(tagName: String) = typeList[tagList.indexOf(tagName)]

    Box(modifier = Modifier) {
        OutlinedButton(
            onClick = { isShow.value = !isShow.value },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = getTypeFromTagName(currentTagName),
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_expand_more_24),
                    contentDescription = null
                )
            }
        }
        DropdownMenu(
            expanded = isShow.value,
            onDismissRequest = { isShow.value = false },
            content = {
                typeList.forEachIndexed { index, type ->
                    DropdownMenuItem(
                        onClick = {
                            onTagSelect(tagList[index])
                            isShow.value = false
                        },
                        content = { Text(text = type) }
                    )
                }
            }
        )
    }
}