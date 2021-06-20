package io.github.takusan23.designbridge.ui.screen

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.BitmapCompat
import androidx.core.net.toUri
import io.github.takusan23.designbridge.R

/**
 * このアプリについて画面
 * */
@Composable
fun KonoAppScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
            backgroundColor = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.design_bridge_android),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier.size(50.dp)
                )
                Text(text = stringResource(id = R.string.app_name), fontSize = 20.sp)
                Spacer(modifier = Modifier.height(50.dp))
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, "https://github.com/takusan23/DesignBridge".toUri())
                        context.startActivity(intent)
                    },
                    content = {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_language_24), contentDescription = null)
                        Text(text = "ソースコード", Modifier.padding(start = 10.dp, end = 10.dp))
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_launch_24), contentDescription = null)
                    }
                )
            }
        }
    }
}