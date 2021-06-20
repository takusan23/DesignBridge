package io.github.takusan23.designbridge.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * タイトルバー。色を白、黒基調に
 * @param modifier Paddingなど
 * @param title タイトル
 * @param icon 左上のアイコン
 * */
@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        navigationIcon = icon,
        actions = actions,
    )
}

/**
 * ドロップダウンメニュー
 *
 * @param isShowMenu 表示する場合はtrue
 * @param menuMap メニューテキストとMainActivityのNavigationのルートの値が入ったmap
 * @param onMenuClick メニュー押したとき
 * @param onShow これ呼ばれたらisShowMenuをfalseにしてね
 * */
@Composable
fun TitleBarDropDown(
    isShowMenu: Boolean,
    menuMap: Map<String, String>,
    onMenuClick: (String) -> Unit,
    onShow: (Boolean) -> Unit
) {
    // ドロップダウンメニュー
    DropdownMenu(
        expanded = isShowMenu,
        onDismissRequest = { onShow(false) }
    ) {
        menuMap.forEach { menu ->
            DropdownMenuItem(onClick = { onMenuClick(menu.value) }) {
                Text(text = menu.key)
            }
        }
    }

}