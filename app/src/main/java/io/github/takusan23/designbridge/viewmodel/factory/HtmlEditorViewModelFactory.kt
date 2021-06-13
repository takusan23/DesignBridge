package io.github.takusan23.designbridge.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.takusan23.designbridge.viewmodel.HtmlEditorViewModel

/**
 * HTML編集画面で使うやつ
 * */
class HtmlEditorViewModelFactory(private val application: Application, private val editHtmlFilePath: String) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HtmlEditorViewModel(application, editHtmlFilePath) as T
    }

}