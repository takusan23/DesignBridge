package io.github.takusan23.designbridge.tool

import android.app.Activity
import android.os.Build
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


object HideKeyboard {

    /**
     * キーボードを非表示にする。
     *
     * IMEで思い出した。XperiaのPOBox Plus返して。あれ使いやすかったのに
     *
     * @param activity Activity
     * */
    fun hideKeyboard(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val insetsControllerCompat = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
            insetsControllerCompat.hide(WindowInsetsCompat.Type.ime())
        }
    }

}