package io.github.takusan23.designbridge.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.takusan23.designbridge.R
import io.github.takusan23.designbridge.ui.component.TitleBar

/**
 * オープンソースライセンス一覧表示
 *
 * @param onBack 戻る押したとき
 * */
@Composable
fun LicenseScreen(onBack: () -> Unit) {

    val core = """
        androidx.core:core-ktx:1.5.0
        
        Apache 2.0
    """.trimIndent()

    val appCompat = """
        androidx.appcompat:appcompat
        
        Apache 2.0
    """.trimIndent()

    val materialDesign = """
        com.google.android.material:material
        
        Apache 2.0
    """.trimIndent()

    val compose = """
        androidx.compose.ui:ui
        androidx.compose.material:material
        androidx.compose.ui:ui-tooling
        androidx.compose.runtime:runtime-livedata
        androidx.navigation:navigation-compose
        androidx.activity:activity-compose
        
        Apache 2.0
    """.trimIndent()

    val activityFragment = """
        androidx.activity:activity-ktx
        androidx.fragment:fragment-ktx
        
        Apache 2.0
    """.trimIndent()

    val jsoup = """
        org.jsoup:jsoup
        
        MIT
    """.trimIndent()

    val coroutine = """
        org.jetbrains.kotlinx:kotlinx-coroutines-android
        
        Apache 2.0
    """.trimIndent()


    val lifecycle = """
        androidx.lifecycle:lifecycle-runtime-ktx
        
        Apache 2.0
    """.trimIndent()

    val licenseList = listOf(core, appCompat, materialDesign, compose, activityFragment, jsoup, coroutine, lifecycle)

    Scaffold(
        topBar = {
            TitleBar(
                title = { Text(text = "ライセンス") },
                icon = { IconButton(onClick = { onBack() }) { Icon(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24), contentDescription = null) } }
            )
        },
        content = {
            LazyColumn {
                items(licenseList) { licenseText ->
                    Text(
                        text = licenseText,
                        modifier = Modifier.padding(5.dp)
                    )
                    Divider()
                }
            }
        }
    )
}