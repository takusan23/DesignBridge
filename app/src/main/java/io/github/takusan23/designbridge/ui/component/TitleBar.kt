package io.github.takusan23.designbridge.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * タイトルバー。色を白、黒基調に
 * @param modifier Paddingなど
 * @param title タイトル
 * */
@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}