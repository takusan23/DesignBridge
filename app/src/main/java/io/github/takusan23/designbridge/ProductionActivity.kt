package io.github.takusan23.designbridge

import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

/**
 * 画面表示(本番環境)を押したときの画面
 *
 * WebViewを全画面に表示させる
 * */
class ProductionActivity : AppCompatActivity() {

    private val webView by lazy { WebView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 全画面モード
        initFullScreen()

        val filePath = intent.getStringExtra("file_path")!!
        webView.apply {
            setWebViewClient(WebViewClient())
            WebView.setWebContentsDebuggingEnabled(true)
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.allowFileAccess = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.displayZoomControls = false
            loadUrl("file://$filePath")
        }

        setContentView(webView)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 戻ることができる場合はWebView戻って、履歴ない場合はなにもしない
        if (event?.keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initFullScreen() {
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.setDecorFitsSystemWindows(false)
            // Android 11 以上と分岐
            window?.insetsController?.apply {
                // StatusBar + NavigationBar 非表示
                hide(WindowInsets.Type.systemBars())
                // スワイプで一時的に表示可能
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // ノッチにも侵略
                window?.attributes?.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else {
            // Android 10 以前。
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

}